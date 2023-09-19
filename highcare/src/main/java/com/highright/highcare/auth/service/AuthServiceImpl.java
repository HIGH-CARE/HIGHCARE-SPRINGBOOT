package com.highright.highcare.auth.service;

import com.highright.highcare.admin.entity.AccessManager;
import com.highright.highcare.admin.repository.AccessManagerRepository;
import com.highright.highcare.auth.dto.AccountDTO;
import com.highright.highcare.auth.dto.LoginMemberDTO;
import com.highright.highcare.auth.dto.TokenDTO;
import com.highright.highcare.auth.entity.AUTHAccount;
import com.highright.highcare.auth.entity.AUTHPassword;
import com.highright.highcare.auth.entity.AUTHRefreshToken;
import com.highright.highcare.auth.repository.AUTHPasswordRepository;
import com.highright.highcare.auth.repository.AccountRepository;
import com.highright.highcare.auth.repository.RefreshTokenRepository;
import com.highright.highcare.exception.IncorrectPasswordExceedException;
import com.highright.highcare.exception.LoginFailedException;
import com.highright.highcare.exception.LoginFailedPasswordException;
import com.highright.highcare.exception.TokenException;
import com.highright.highcare.jwt.TokenProvider;
import com.highright.highcare.oauth.dto.GoogleUser;
import com.highright.highcare.oauth.dto.KakaoUser;
import com.highright.highcare.oauth.dto.OAuthUserInfo;
import com.highright.highcare.oauth.entity.OAuthUser;
import com.highright.highcare.oauth.repository.OAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final OAuthRepository oAuthRepository;
    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AUTHPasswordRepository passwordRepository;
    private final AccessManagerRepository accessManagerRepository;
    private final PasswordEncoder passwordEncoder;

    private final String SOCIAL_LOGIN_TYPE = "oauth";   // 로그인타입(소셜인경우만)


    @Transactional(noRollbackFor = {LoginFailedException.class, IncorrectPasswordExceedException.class, LoginFailedPasswordException.class})
    @Override
    public Object selectLogin(LoginMemberDTO loginInfo, HttpServletResponse response) {

        Optional<AccessManager> accessManager = accessManagerRepository.findById(loginInfo.getId());
        log.info("[AuthServiceImpl] selectLogin : accessManager======{}", accessManager);

        LoginMemberDTO setMemberDTO = this.setMember(loginInfo, accessManager);

        // access 토큰 + refresh 토큰 받아오기
        TokenDTO token = tokenProvider.generateTokenDTO(setMemberDTO);
        Cookie cookie = tokenProvider.generateRefreshTokenInCookie(setMemberDTO);

        log.info("[AuthServiceImpl] login : cookie======{}", cookie);
        log.info("[AuthServiceImpl] login : token======{}", token);

        // 헤더에 쿠키 저장
        response.addCookie(cookie);
        try {
            // 리프레시토큰 레디스에 id와 저장
            refreshTokenSave(AUTHRefreshToken.builder().id(setMemberDTO.getId())
                    .refreshToken(cookie.getValue().split("=")[0])
                    .build());

            // 로그인 이력관리
            accessManager.get().setRegistDate(LocalDateTime.now());
            accessManager.get().setLoginTotalCount(accessManager.get().getLoginTotalCount() + 1);
            accessManager.get().setLoginFailCount(0);
            accessManager.get().setBrowser(loginInfo.getBrowser());
            accessManager.get().setDevice(loginInfo.getDevice());

            return token;
        } catch (Exception e) {
            throw new TokenException("토큰 발급 실패, 다시 시도해주세요.");
        }
    }


    // 멤버조회
    private AUTHAccount memberFindById(String id) {
        AUTHAccount member = accountRepository.findByMemberId(id);
        log.info("[AuthServiceImpl] memberFindByIdz : member======{}", member);

        if (member == null) {
            throw new LoginFailedException("ID not found : " + id + "를 찾을 수 없습니다");
        }
        return member;
    }

    // 리프레쉬토큰을 통한 엑세스토큰 재발행
    @Override
    public Object reIssueToken(HttpServletRequest request) {

        // 리프레시토큰 쿠키 resolve
        AUTHRefreshToken refreshToken = tokenProvider.resolveCookie(request);
        log.info("[AuthServiceImpl] reIssueToken : refreshToken======{}", refreshToken);

        // 리프레시토큰 db와 일치하는지 검증 - true면 access토큰 발급
        if (findRefreshToken(refreshToken)) {

            return tokenProvider.generateTokenDTO(modelMapper
                    .map(memberFindById(refreshToken.getId()), LoginMemberDTO.class));
        }

        throw new TokenException("액세트 토큰 재발급 실패");
    }


    // 소셜로그인 연동 데이터 인서트, jwt토큰 발급 요청
    @Override
    @Transactional(noRollbackFor = {LoginFailedException.class, IncorrectPasswordExceedException.class})
    public Object insertOauthRegist(Map<String, Object> data, HttpServletResponse response) {

        OAuthUserInfo userInfo = null;
        switch ((String) data.get("provider")) {

            case "google":
                userInfo = new GoogleUser(data);
                break;
            case "kakao":
                userInfo = new KakaoUser(data);
                break;
        }

        log.info("[AuthServiceImpl] insertOauthRegist : googleUser.getProvider ================{}", userInfo.getProvider());
        log.info("[AuthServiceImpl] insertOauthRegist : googleUser.getProviderId ================{}", userInfo.getProviderId());

        // 기존 계정 있는지 조회 : 리소스서버(프로바이더)명 + 프로바이더에서 제공한 고유아이디
        Optional<OAuthUser> findOauthUser = oAuthRepository.findById(userInfo.getProvider() + "_" + userInfo.getProviderId());
        OAuthUser insertUser = null;
        Optional<AccessManager> accessManager = accessManagerRepository.findById(findOauthUser.get().getId());


        //기존 계정 조회해서 없으면
        if (findOauthUser.isEmpty()) {
            insertUser = OAuthUser.builder()
                    .oauthId(userInfo.getProvider() + "_" + userInfo.getProviderId())
                    .provider(userInfo.getProvider())
                    .id(userInfo.getId())
                    .provideEmail(userInfo.getEmail())
                    .provideName(userInfo.getName())
                    .build();

            log.info("[AuthServiceImpl] insertOauthRegist : insertUser ================{}", insertUser);

            oAuthRepository.save(insertUser);
            // 로그인 이력에도 신규등록
            if (accessManager.isEmpty()) {

                accessManagerRepository.save(AccessManager.builder().id(insertUser.getId())
                        .registDate(LocalDateTime.now())
                        .loginFailCount(0)
                        .loginTotalCount(0)
                        .isLock("N")
                        .isInActive("N")
                        .isExpired("N")
                        .isWithDraw("N")
                        .browser((String) data.get("browser"))
                        .device((String) data.get("device"))
                        .build());
            }
        }
        // 기존 계정 존재하면 업데이트
        else {
            insertUser = findOauthUser.get();
            log.info("[AuthServiceImpl] insertOauthRegist : findOauthUser.get() = insertUser ================{}", insertUser);
            accessManager.get().setRegistDate(LocalDateTime.now());
            accessManager.get().setLoginTotalCount(accessManager.get().getLoginTotalCount() + 1);
            accessManager.get().setBrowser((String) data.get("browser"));
            accessManager.get().setDevice((String) data.get("device"));
        }


        // 소셜 아이디 가져가서 토큰 발행
        LoginMemberDTO setMemberDTO = this.setMember((LoginMemberDTO.builder().id(insertUser.getId()).loginType(SOCIAL_LOGIN_TYPE).build()), accessManager);

        // access 토큰 + refresh 토큰 받아오기
        TokenDTO token = tokenProvider.generateTokenDTO(setMemberDTO);
        Cookie cookie = tokenProvider.generateRefreshTokenInCookie(setMemberDTO);

        log.info("[AuthServiceImpl] insertOauthRegistlogin : cookie======{}", cookie);
        log.info("[AuthServiceImpl] insertOauthRegistlogin : token======{}", token);

        // 헤더에 쿠키 저장
        response.addCookie(cookie);
        try {
            // 리프레시토큰 레디스에 id와 저장
            refreshTokenSave(AUTHRefreshToken.builder().id(setMemberDTO.getId())
                    .refreshToken(cookie.getValue().split("=")[0])
                    .build());
            return token;
        } catch (Exception e) {
            throw new TokenException("토큰 발급 실패, 다시 시도해주세요.");
        }
    }

    @Transactional
    @Override
    public Object updateAndInsertPassword(AccountDTO accountDTO) {

        Optional<AUTHAccount> authAccount = accountRepository.findById(accountDTO.getMemberId());
        Optional<AccessManager> accessManager = accessManagerRepository.findById(accountDTO.getMemberId());

        log.info("[AuthServiceImpl] updateAndInsertPassword : authAccount======{}", authAccount);

        if (authAccount.isPresent()) {

            AUTHPassword authPassword = AUTHPassword.builder()
                    .prevPassword(authAccount.get().getPassword())
                    .id(authAccount.get().getMemberId())
                    .changeDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            log.info("[AuthServiceImpl] updateAndInsertPassword : authPassword======{}", authPassword);

            // 비밀번호 변경이력 테이블 insert
            passwordRepository.save(authPassword);
            // 계정 테이블 비밀번호 변경 업데이트
            authAccount.get().setPassword(passwordEncoder.encode(accountDTO.getPassword()));
            authAccount.get().setIsTempPwd("N");
            // 비밀번호 변경하면 잠금 풀기
            accessManager.get().setIsLock("N");


            return "비밀번호 업데이트 완료";
        } else {
            return "비밀번호 업데이트 실패";
        }

    }


    @Transactional
    public void refreshTokenSave(AUTHRefreshToken refreshToken) {
        log.info("[AuthServiceImpl] refreshTokenSave : refreshToken ==== {}", refreshToken);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public boolean findRefreshToken(AUTHRefreshToken refreshToken) {
        log.info("[AuthServiceImpl] findRefreshToken : refreshToken ==== {}", refreshToken);

        Optional<AUTHRefreshToken> findRefreshToken = (refreshTokenRepository.findById(refreshToken.getId()));
        if (findRefreshToken.isPresent() && findRefreshToken.get().getRefreshToken().equals(refreshToken.getRefreshToken())) {
            log.info("[AuthServiceImpl] refreshTokenSave : findRefreshToken ==== {}", findRefreshToken);
            return true;
        }
        throw new TokenException("리프레시토큰 검증 실패하였습니다. 재로그인 해주세요.");
    }

    @Transactional(noRollbackFor = {LoginFailedException.class, IncorrectPasswordExceedException.class, LoginFailedPasswordException.class})
    public LoginMemberDTO setMember(LoginMemberDTO loginInfo, Optional<AccessManager> accessManager) {

        log.info("[AuthServiceImpl] setMember : loginInfo ====== {} ", loginInfo);
        log.info("[AuthServiceImpl] setMember : accessManager ====== {} ", accessManager);

        // 일반로그인시 진행
        // 현재 loginFailCount 값을 가져와서 1을 더하고 업데이트
        int currentFailCount = accessManager.get().getLoginFailCount();
        log.info("[AuthServiceImpl] setMember : currentFailCount ====== {} ", currentFailCount);

        try {

            // 멤버 조회
            AUTHAccount member = memberFindById(loginInfo.getId());
            log.info("[AuthServiceImpl] setMember : member ====== {} ", member);

            LoginMemberDTO setMemberDTO = modelMapper.map(member, LoginMemberDTO.class);
            log.info("[AuthServiceImpl] setMember : setMemberDTO ====== {} ", setMemberDTO);

            // 접속관리 데이터 있으면
            // 성공시 token 발급
            // 멤버 dto에 다시 여러 정보 담아서 전달하기
            log.info("[AuthServiceImpl] login : loginMemberDTO======{}", setMemberDTO);
            setMemberDTO.setEmpNo(member.getEmployee().getEmpNo());
            setMemberDTO.setName(member.getEmployee().getName());
            setMemberDTO.setDeptName(member.getEmployee().getDeptCode().getDeptName());
            setMemberDTO.setJobName(member.getEmployee().getJobCode().getJobName());

            if(loginInfo.getPassword() == null && loginInfo.getLoginType().matches(SOCIAL_LOGIN_TYPE)){
                return setMemberDTO;

            } else if(!passwordEncoder.matches(loginInfo.getPassword(), member.getPassword())) {
                accessManagerRepository.save(accessManager.get()); // 엔티티 업데이트

                throw new LoginFailedPasswordException(" [PASSWORD INCORRECT] 잘못된 비밀번호입니다.");
            }
            return setMemberDTO;

        } catch (LoginFailedException e) {
            // 실패카운트 5이상이면
            if (currentFailCount >= 5) {
                // 5회 미만 틀린 경우 catch에서 실패 카운트 1회 추가
                accessManager.get().setIsLock("Y");
                throw new IncorrectPasswordExceedException(" [PASSWORD INCORRECT] 비밀번호 오류 5회 초과");
            }
            throw e;
        }
    }
}
