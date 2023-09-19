package com.highright.highcare.reservation.service;

import com.highright.highcare.bulletin.entity.Board;
import com.highright.highcare.bulletin.entity.BulletinEmployee;
import com.highright.highcare.bulletin.repository.BulletinEmployeeRepository;
import com.highright.highcare.common.Criteria;
import com.highright.highcare.exception.ResException;
import com.highright.highcare.reservation.dto.ResourceCategoryDTO;
import com.highright.highcare.reservation.dto.ResourceDTO;
import com.highright.highcare.reservation.dto.ResourceFileDTO;
import com.highright.highcare.reservation.dto.ResourceReservationStatusDTO;
import com.highright.highcare.reservation.entity.*;
import com.highright.highcare.reservation.repository.ResourceCategoryRepository;
import com.highright.highcare.reservation.repository.ResourceFileRepository;
import com.highright.highcare.reservation.repository.ResourceRespository;
import com.highright.highcare.reservation.repository.ResourceStatusRepository;
import com.highright.highcare.util.FileUploadUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResService {

    private final ResourceCategoryRepository resourceCategoryRepository;
    private final ResourceRespository resourceRespository;
    private final ResourceFileRepository resourceFileRepository;
    private final ResourceStatusRepository resourceStatusRepository;

    private final BulletinEmployeeRepository bulletinEmployeeRepository;
    private final ModelMapper modelMapper;
    @Value("${image.image-dir}")
    String IMAGE_DIR; // src/main/resources/static/images

    @Value("${image.image-url}")
    String IMAGE_URL; //http://highcare.coffit.today:8080/images/
    public ResService(ResourceCategoryRepository resourceCategoryRepository,
                      ModelMapper modelMapper,
                      ResourceRespository resourceRespository,
                      ResourceFileRepository resourceFileRepository,
                      ResourceStatusRepository resourceStatusRepository,
                      BulletinEmployeeRepository bulletinEmployeeRepository){
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.modelMapper = modelMapper;
        this.resourceRespository = resourceRespository;
        this.resourceFileRepository =resourceFileRepository;
        this.resourceStatusRepository = resourceStatusRepository;
        this.bulletinEmployeeRepository = bulletinEmployeeRepository;
    }
    public List<ResourceCategoryDTO> selectResCategory() {
        List<ResourceCategory> resList = resourceCategoryRepository.findAll();
        return resList.stream()
                .map(res -> modelMapper.map(res, ResourceCategoryDTO.class)).collect(Collectors.toList());
    }
    @Transactional
    public Object insertCategory(ResourceCategoryDTO resourceCategory) {
    ResourceCategory res = modelMapper.map(resourceCategory, ResourceCategory.class);

    resourceCategoryRepository.save(res);

    resourceCategory.setCategoryCode(res.getCategoryCode());
    return resourceCategory;

    }
    @Transactional
    public Object insertResStatus(ResourceReservationStatusDTO resourceReservationStatusDTO) throws Exception {
        Resource resource = resourceRespository.findById(resourceReservationStatusDTO.getResourceCode()).get();
        BulletinEmployee bulletinEmployee = bulletinEmployeeRepository.findById(resourceReservationStatusDTO.getEmpNo()).get();
        ResourceReservationStatus res = modelMapper.map(resourceReservationStatusDTO, ResourceReservationStatus.class);
        res.setBulletinEmployee(bulletinEmployee);
        res.setResource(resource);
//        String reservationDate = res.getReservationDate();
        java.sql.Date reservationDate = res.getReservationDate();
        String startTime = res.getStartTime();
        String endTime = res.getEndTime();
        int resourceCode =resourceReservationStatusDTO.getResourceCode();
        res.setReservationStatus("SCREENING");
        List<ResourceReservationStatus> resList = resourceStatusRepository.findByresList(reservationDate, startTime, endTime, resourceCode);

        if(resList.size() ==0){
            resourceStatusRepository.save(res);
            return "예약성공";
        }else{
            throw new ResException("예약에 실패하셨습니다. 시간을 다시 확인 바랍니다");
        }

    }
    @Transactional
    public Object updateRes(ResourceDTO resourceDTO, MultipartFile image) throws IOException{
        java.util.Date utilDate = new java.util.Date();
        long currentMilliseconds = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(currentMilliseconds);


        Resource resource = resourceRespository.findById(resourceDTO.getResourceCode()).get();
        resource.setResourceCategory(resourceCategoryRepository.findById(resourceDTO.getCategoryCode()).get());
        resource.setArea(resourceDTO.getArea());
        resource.setLocation(resourceDTO.getLocation());
        resource.setServiceGuide(resourceDTO.getServiceGuide());
        resource.setModifiedDate(sqlDate);
        resource.setStartTime(resourceDTO.getStartTime());
        resource.setEndTime(resourceDTO.getEndTime());
        resource.setResourceName(resourceDTO.getResourceName());
        resourceRespository.save(resource);

        resourceFileRepository.deleteByResourceCode(resource.getResourceCode());
        String imageName = UUID.randomUUID().toString().replace("-","");
        String replaceFileName = null;

        int result = 0;
        ResourceFileDTO resourceFileDTO = new ResourceFileDTO();
        try{
            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, image);
            String originName = image.getOriginalFilename();
            String fileExtension = originName.substring(originName.lastIndexOf(".") + 1);

            resourceFileDTO.setChangedFileName(imageName);
            resourceFileDTO.setModifiedDate(sqlDate);
            resourceFileDTO.setCreationDate(sqlDate);
            resourceFileDTO.setDeleteYn('N');
            resourceFileDTO.setResourceCode(resource.getResourceCode());
            resourceFileDTO.setType(fileExtension);
            resourceFileDTO.setOriginalFileName(originName);
            ResourceFile resourceFile = modelMapper.map(resourceFileDTO, ResourceFile.class);
            resourceFileRepository.save(resourceFile);

            result = 1;
        } catch (Exception e) {
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }
        return (result > 0) ? " 수정 성공": "수정 실패";
    }
    @Transactional
    public Object insertRes(ResourceDTO resourceDTO, MultipartFile image) throws IOException {

        java.util.Date utilDate = new java.util.Date();
        long currentMilliseconds = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(currentMilliseconds);

        Resource resource = modelMapper.map(resourceDTO, Resource.class);
        resource.setDeleteYn('N');
        resource.setCreationDate(sqlDate);
        resource.setModifiedDate(sqlDate);

        resource.setResourceCategory(resourceCategoryRepository.findById(resourceDTO.getCategoryCode()).get());

        resourceRespository.save(resource);


        String imageName = UUID.randomUUID().toString().replace("-","");
        String replaceFileName = null;

        int result = 0;
        ResourceFileDTO resourceFileDTO = new ResourceFileDTO();
        try{
            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, image);
            String originName = image.getOriginalFilename();
            String fileExtension = originName.substring(originName.lastIndexOf(".") + 1); // ex) jpg
            resourceFileDTO.setChangedFileName(imageName);
            resourceFileDTO.setModifiedDate(sqlDate);
            resourceFileDTO.setCreationDate(sqlDate);
            resourceFileDTO.setDeleteYn('N');
            resourceFileDTO.setResourceCode(resource.getResourceCode());
            resourceFileDTO.setType(fileExtension);
            resourceFileDTO.setOriginalFileName(originName);

            ResourceFile resourceFile = modelMapper.map(resourceFileDTO, ResourceFile.class);
            resourceFileRepository.save(resourceFile);

            result = 1;
        } catch (Exception e) {
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }
        return (result > 0) ? " 입력 성공": "입력 실페";

    }

    public Object selectRes(int categoryCode) {
        List<ResourceArea> resources= resourceRespository.findByResourceCategoryAndDeleteYn(resourceCategoryRepository.findById(categoryCode).get(), 'N');
        return resources.stream()
                .map(res-> modelMapper.map(res,ResourceDTO.class)).collect(Collectors.toList());
    }

    public Object selectContent(int resourceCode) {
        Resource resource = resourceRespository.findById(resourceCode).get();
        ResourceDTO resource1 = modelMapper.map(resource, ResourceDTO.class);
        ResourceFile resourceFile = resourceFileRepository.findByResourceCode(resourceCode);
        resource1.setFileUrl("http://highcare.coffit.today:8080/getImage/"+resourceFile.getChangedFileName()+"/"+resourceFile.getType());
        return resource1;

    }

    public Object selectDate(String reservationDate,int resourceCode) {
        System.out.println("reservationDate" + reservationDate);
        System.out.println("resourceCode" + resourceCode);
        List<ResourceReservationStatus> resourceReservationStatuses = resourceStatusRepository.selectStatus(reservationDate,resourceCode);
        System.out.println("list" + resourceReservationStatuses);
        return resourceReservationStatuses.stream()
                .map(status -> modelMapper.map(status, ResourceReservationStatusDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public Object deleteRes(ResourceDTO resourceDTO) {

        java.util.Date utilDate = new java.util.Date();
        long currentMilliseconds = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(currentMilliseconds);
        Resource resource = resourceRespository.findById(resourceDTO.getResourceCode()).get();
        resource.setModifiedDate(sqlDate);
        resource.setDeleteYn('Y');
        resourceRespository.save(resource);
        return "삭제 성공";

    }

    public int selectResTotal(int empNo) {
        BulletinEmployee bulletinEmployee = bulletinEmployeeRepository.findById(empNo).get();

        List<ResourceReservationStatus> resourceReservationStatuses = resourceStatusRepository.findByBulletinEmployee(bulletinEmployee);

        return resourceReservationStatuses.size();
    }

    public Object selectStatusListWithPaging(Criteria cri, int empNo) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("statusCode").descending());
        BulletinEmployee bulletinEmployee = bulletinEmployeeRepository.findById(empNo).get();
        Page<ResourceReservationStatus> result = resourceStatusRepository.findByBulletinEmployee(bulletinEmployee, paging);

        List<ResourceReservationStatus> resourceReservationStatuses = (List<ResourceReservationStatus>) result.getContent();
        return resourceReservationStatuses.stream().map(statusRes -> modelMapper.map(statusRes, ResourceReservationStatusDTO.class)).collect(Collectors.toList());

    }

    public int selectAllResTotal() {
        List<ResourceReservationStatus> resourceReservationStatuses = resourceStatusRepository.findAll();

        return resourceReservationStatuses.size();
    }

    public Object selectAllStatusListWithPaging(Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("statusCode").descending());
        Page<ResourceReservationStatus> result = resourceStatusRepository.findAll(paging);
        List<ResourceReservationStatus> resourceReservationStatuses = (List<ResourceReservationStatus>) result.getContent();
        return resourceReservationStatuses.stream().map(statusRes -> modelMapper.map(statusRes, ResourceReservationStatusDTO.class)).collect(Collectors.toList());


    }
    @Transactional
    public Object updateResStatus(ResourceReservationStatusDTO resourceReservationStatusDTO) {
        int statusCode = resourceReservationStatusDTO.getStatusCode();
        ResourceReservationStatus reservationStatus=resourceStatusRepository.findById(statusCode).get();
        reservationStatus.setReservationStatus("APPROVAL");

        return "수정 성공";
    }
    @Transactional
    public Object updateResrejected(ResourceReservationStatusDTO resourceReservationStatusDTO) {
        int statusCode = resourceReservationStatusDTO.getStatusCode();
        ResourceReservationStatus reservationStatus=resourceStatusRepository.findById(statusCode).get();
        reservationStatus.setReservationStatus("REJECTED");

        return "거절 성공";
    }
}
