package com.highright.highcare.auth.service;

import com.highright.highcare.auth.dto.LoginMemberDTO;
import com.highright.highcare.auth.entity.AUTHAccount;
import com.highright.highcare.auth.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        AUTHAccount account = accountRepository.findByMemberId(memberId);
        LoginMemberDTO AuthenticMember = modelMapper.map(account, LoginMemberDTO.class);

        log.info("[CustomUserDetailsService] loadUserByUsername : AuthenticMember === {}", AuthenticMember);
        List<GrantedAuthority> authorities = new ArrayList<>();
        AuthenticMember.getRoleList().stream()
                .map(role -> role.getAuthCode()).distinct()
                .map(auth -> authorities.add(new SimpleGrantedAuthority(auth)))
                .collect(Collectors.toList());



        AuthenticMember.setAuthorities(authorities);
        log.info("[CustomUserDetailsService] loadUserByUsername : authorities === {}", authorities);
        log.info("[CustomUserDetailsService] loadUserByUsername : AuthenticMember.getAuthorities === {}", AuthenticMember.getAuthorities());


        return AuthenticMember;
    }
}
