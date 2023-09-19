package com.highright.highcare.auth.service;

import com.highright.highcare.auth.dto.AUTHFindAccountDTO;
import com.highright.highcare.auth.dto.MailDTO;
import com.highright.highcare.auth.entity.AUTHAccount;
import com.highright.highcare.auth.entity.AUTHFindAccount;
import com.highright.highcare.auth.repository.AccountRepository;
import com.highright.highcare.auth.repository.FindAccountRepository;
import com.highright.highcare.common.AdminCustomBean;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class FindAccountService {

    private JavaMailSender javaMailSender;
    private AccountRepository accountRepository;
//    private ADMEmployeeRepository admEmployeeRepository;
    private AdminCustomBean adminCustomBean;
    private FindAccountRepository findAccountRepository;
    private static final String FROM_ADDRESS = "highcaretest@high.or.kr";

    @Transactional
    public Object emailSendAuth(MailDTO mailDTO){

        Map<String, Object> result = new HashMap<>();

        AUTHAccount account = accountRepository.findByEmployee_EmailAndEmployee_Name(
                mailDTO.getMail(), mailDTO.getName());

        if (account != null) {
            String authCode = adminCustomBean.randomPassword().substring(0,6);

            mailDTO.setTitle("[하이케어] 비밀번호 인증링크 발송 ");
            mailDTO.setMessage("비밀번호 초기화 인증코드 : " + authCode);

            /* 레디스에 3분간 인증번호 저장 (인증수단 주소/번호 - 부여 인증번호) */
            findAccountRepository.save(new AUTHFindAccount(mailDTO.getMail(), authCode));

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mailDTO.getMail());
            message.setSubject(mailDTO.getTitle());
            message.setText(mailDTO.getMessage());
            javaMailSender.send(message);

            result.put("code", true);
            result.put("message", "입력하신 이메일로 인증번호가 발송되었습니다.");
            result.put("timeout", 180);
            return result;
        } else{
            result.put("code", false);
            result.put("message", "회원가입시 등록한 이름과 이메일주소가 일치하지 않습니다.");
            return result;
        }
    }

    public Object selectAuthNumberCheck(AUTHFindAccountDTO authFindAccountDTO) {
        log.info("FindAccountService selectAuthNumberCheck authFindAccountDTO === {}",  authFindAccountDTO);

        Map<String, Object> result = new HashMap<>();

        // 인증코드 불러오기
        Optional<AUTHFindAccount> authFindAccount = findAccountRepository.findById(authFindAccountDTO.getId());
        log.info("FindAccountService selectAuthNumberCheck authFindAccount ==={} ", authFindAccount);

        // 인증코드 존재 유무
        if(authFindAccount.isPresent()){
            log.info("FindAccountService selectAuthNumberCheck authFindAccount.isPresent()");

            // 입력 인증코드 correct 체크
            if(authFindAccount.get().getAuthCode().matches(authFindAccountDTO.getAuthCode())){
                log.info("FindAccountService selectAuthNumberCheck  matches");
                // 해당 인증id(이메일/폰번호)로 계정 찾아오기
                AUTHAccount findAccount = accountRepository.findByEmployee_EmailAndEmployee_Name(authFindAccountDTO.getId(), authFindAccountDTO.getName());

                result.put("findId", findAccount.getMemberId());
                result.put("requestMessage", "correct");
            }
            else {
                result.put("requestMessage", "incorrect");
            }
        }

    return  result;

    }
}
