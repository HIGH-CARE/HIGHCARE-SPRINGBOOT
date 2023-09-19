package com.highright.highcare.mypage.service;

//import com.highright.highcare.mypage.Repository.AnnRepository;
import com.highright.highcare.common.Criteria;
import com.highright.highcare.mypage.Repository.AnnRepository;
//import com.highright.highcare.mypage.Repository.MyManagementRepository;
import com.highright.highcare.mypage.Repository.MyManagementRepository;
import com.highright.highcare.mypage.dto.MyAnnualDTO;
import com.highright.highcare.mypage.dto.MyManegementDTO;
import com.highright.highcare.mypage.entity.*;
import com.highright.highcare.util.FileUploadUtils;
import com.highright.highcare.mypage.Repository.MyProfileFileRepository;
import com.highright.highcare.mypage.Repository.ProfileRepository;
import com.highright.highcare.mypage.dto.MyProfileDTO;
import com.highright.highcare.mypage.dto.MyProfileFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MypageService {

    private final ProfileRepository profileRepository;

    private final MyProfileFileRepository myProfileFileRepository;

    private final AnnRepository annRepository;

    private final MyManagementRepository myManagementRepository;
    private final ModelMapper modelMapper;


    @Value("C:/dev/profileImages/")
    private String IMAGE_DIR;
    @Value("http://highcare.coffit.today:8080/images/")
    private String IMAGE_URL;

    @Autowired
    public MypageService(ProfileRepository profileRepository, MyProfileFileRepository myProfileFileRepository, AnnRepository annRepository, ModelMapper modelMapper, MyManagementRepository myManagementRepository) {
        this.profileRepository = profileRepository;
        this.myProfileFileRepository = myProfileFileRepository;
        this.annRepository = annRepository;
        this.myManagementRepository = myManagementRepository;

        this.modelMapper = modelMapper;
    }


//    @Transactional
//    public MyProfileDTO selectProfilefileList(int empNo) {
//        log.info("[MypageService] selectProfile Start =============================================");
//        MyProfile ProfileList = profileRepository.findByEmpNo(empNo);
//        log.info("[MypageService] selectProfilefileList ProfileList =================={}", ProfileList);
//        log.info("[MypageService] selectProfile End =============================================");
//
//        MyProfileDTO myProfileDTOList = modelMapper.map(ProfileList, MyProfileDTO.class);
//        log.info("[MypageService] selectProfilefileList myProfileDTO1111 =================={}", myProfileDTOList);
//
//
//        return myProfileDTOList;
//    }
@Transactional
public List<MyProfileDTO> selectProfilefileList(int empNo) {
    log.info("[MypageService] selectProfile Start =============================================");
    List<MyProfile> profileList = profileRepository.findByEmpNo(empNo);

    List<MyProfileDTO> myProfileDTOList = profileList.stream()
            .map(profile -> modelMapper.map(profile, MyProfileDTO.class))
            .collect(Collectors.toList());

    log.info("[MypageService] selectProfilefileList myProfileDTOList =================={}", myProfileDTOList);
    log.info("[MypageService] selectProfile End =============================================");

    return myProfileDTOList;
}
//
//        return myProfileDTO;
//    }

    @Transactional
    public MyProfileFileDTO updateMyProfileFile(MyProfileFileDTO myProfileFileDTO, MultipartFile profileImage) {
        log.info("[MypageService] updateMyProfileFile --------------- start ");
        log.info("[MypageService] update MyProfileFileDTO!!@@@ " + myProfileFileDTO);

        try {
            Optional<MyProfileFile> existingProfileFileOptional = myProfileFileRepository.findByCode(myProfileFileDTO.getCode());

            System.out.println("existingProfileFileOptional =============== " + existingProfileFileOptional);
            if (existingProfileFileOptional.isPresent()) {
                MyProfileFile existingProfileFile = existingProfileFileOptional.get();

                String imageName = UUID.randomUUID().toString().replace("-", "");
                String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, profileImage);

                existingProfileFile.setName(profileImage.getOriginalFilename());
                existingProfileFile.setChName(replaceFileName);
                existingProfileFile.setDate(new Date(System.currentTimeMillis()));

                String imageUrl = IMAGE_URL + replaceFileName;
                existingProfileFile.setProfileImgUrl(imageUrl);

                existingProfileFile = myProfileFileRepository.save(existingProfileFile);

                log.info("ProfileService update 이미지 oname : {}", profileImage.getOriginalFilename());
                log.info("ProfileService update 이미지 name : {}", replaceFileName);
                MyProfileFileDTO result = modelMapper.map(existingProfileFile, MyProfileFileDTO.class);
                System.out.println("result = " + result);

                return result;

            } else {
                throw new RuntimeException("해당 코드의 프로필 파일을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("[MypageService] Error updateMyProfileFile : " + e.getMessage());
            throw new RuntimeException("파일 업데이트 실패");
        }
    }


    @Transactional
    public List<MyAnnualDTO> selectAnnList(int empNo) {
        log.info("[MyPageService] selectAnnList empNo^^^^ {}", empNo);

        List<MyAnnual> myAnnuals = annRepository.findByEmpNo(empNo);

        log.info("[MyPageService] selectAnnList annEmployee^^^^ {}", myAnnuals);

        List<MyAnnualDTO> myAnnualDTOList = myAnnuals.stream().map(item -> modelMapper.map(item, MyAnnualDTO.class)).collect(Collectors.toList());
        log.info("[MyPageService] MyEmployeeDTO^^^^ {}",myAnnualDTOList);

        return myAnnualDTOList;
    }

    /* 토탈 */
    public int annselectTotal(int empNo) {

        log.info("[Service annselectTotal] annselectTotal --------------- start ");
        List<MyAnnual> myAnnualList = annRepository.findByEmpNo(empNo); // 회원번호 전체 정보 조회

        log.info("[Service annselectTotal] myAnnualList.size: {}", myAnnualList.size());
        log.info("[Service annselectTotal] annselectTotal --------------- end ");

        return myAnnualList.size();

    }


    public Object selectAnnListPaging(int empNo, Criteria cri) {
        log.info("[MypageService] selectAnnListPaging => start ======");

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("apvNo").descending());

        Page<MyAnnual> result1 = annRepository.findByEmpNo(empNo, paging);
        log.info("[MypageService] selectAnnListPagin result1 =>=============" + paging);


        log.info("[MypageService] selectAnnListPaging => end =============");

        return result1.stream().map(MyAnnual -> modelMapper.map(MyAnnual, MyAnnualDTO.class)).collect(Collectors.toList());
    }


    public List<MyManegementDTO> selectManList(int empNo) {
        log.info("[MyPageService] empNo%%%%%% {}", empNo);
        List<MyManegement> myManegementList = myManagementRepository.findByEmpNo(empNo);
       List<MyManegementDTO> mymanagementDTOList = myManegementList.stream().map(item -> modelMapper.map(item, MyManegementDTO.class)).collect(Collectors.toList());


        log.info("[MyPageService] myManegementList ========== {}", myManegementList);
        log.info("[MyPageService] managementDTOList =============== {}", mymanagementDTOList);

        return mymanagementDTOList;
    }

    public int manselectTotal(int empNo) {

        log.info("[Service] manselectTotal Start ===================");

        List<MyManegement> myManList = myManagementRepository.findByEmpNo(empNo);
        log.info("[Service] manselectTotal.size : {}", myManList.size());

        log.info("[Service manselectTotal] myAnnualList.size: {}", myManList.size());
        log.info("[Service] manselectTotal End ===================");

        return myManList.size();
    }

    public Object selectManListWithPaging(int empNo, Criteria cri) {

        log.info("[Service] selectManListWithPaging Start ======");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("manNo").descending());

        Page<MyManegement> result1 = myManagementRepository.findByEmpNo(empNo, paging);
        log.info("[MypageService] selectmanListPaging result1 =>=============" + paging);


        log.info("[MypageService] selectManListWithPaging => end =======");

        return result1.stream().map(mymanagement -> modelMapper.map(mymanagement, MyManegementDTO.class)).collect(Collectors.toList());
    }
}

