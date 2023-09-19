package com.highright.highcare.approval.dto;
import com.highright.highcare.approval.entity.ApvEmployee;
import com.highright.highcare.pm.dto.PmEmployeeDTO;
import lombok.*;
import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvFormDTO {

    private Long apvNo;
    private String title;
    private Date writeDate;
    private String apvStatus;
    private String isUrgency;
    private String category;
    private String contents1;
    private String contents2;
    private String totalAmount;
    private Long refApvNo;
    private int empNo;

    private ApvEmployee apvEmployee;
    private String empName;
    private String deptName;
    private String jobName;


    /* 업무 */
    private List<ApvMeetingLogDTO> apvMeetingLogs;
    private List<ApvBusinessTripDTO> apvBusinessTrips;
    private List<ApvOfficialDTO> apvOfficials;

    /* 지출 */
    private List<ApvExpFormDTO> apvExpForms;
    private List<ApvFamilyEventDTO> apvFamilyEvents;
    private List<ApvCorpCardDTO> apvCorpCards;

    /* 인사 */
    private List<ApvVacationDTO> apvVacations;
    private List<ApvIssuanceDTO> apvIssuances;

    /* 결재라인*/
    private List<ApvLineDTO> apvLines;
    private List<ApvFileDTO> apvFiles;

    public void getEmployeeDTO() {
        if (apvEmployee != null) {
            this.empName = apvEmployee.getName();
            this.deptName = apvEmployee.getDeptCode().getDeptName();
            this.jobName = apvEmployee.getJobCode().getJobName();
        }
    }

}
