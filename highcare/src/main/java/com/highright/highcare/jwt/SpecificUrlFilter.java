package com.highright.highcare.jwt;

import com.highright.highcare.admin.dto.MenuDTO;
import com.highright.highcare.admin.dto.MenuGroupDTO;
import com.highright.highcare.admin.entity.Menu;
import com.highright.highcare.admin.repository.MenuRepository;
import com.highright.highcare.exception.NotAllowedRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.ModelMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@Order(3)
@Component
@Slf4j
@RequiredArgsConstructor
@WebFilter(urlPatterns = "/*")
public class SpecificUrlFilter implements Filter {

    private final TokenProvider tokenProvider;
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;
    private final List<String> allowedStartUrls =
            Arrays.asList(  // 매니저 권한만 접근가능한 api 요청 url
              "/api/admin"                // 관리자
            , "/api/pm/member/all"      // 사원등록
            );
    @Transactional
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        // 요청 URI가 권한이 필요한 uri이면 권한 체크 시작
        if (isAllowedStartUrl(requestURI)) {

            // 토큰에서 id 추출
            String jwt = tokenProvider.resolveToken(((HttpServletRequest) request));
            String id = tokenProvider.parseClaims(jwt).getSubject();

            // 토큰 아이디로 디비에서 해당 아이디로 접근 허용된 url을 조회해온다.
            List<Menu> menuList = (List<Menu>) menuRepository.findById(id);
            List<MenuDTO> menuDTOList = menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());

            // 조회해온 url을 돌며 요청 uri가 포함하고 있는 주소인지 체크
            boolean isAllowed = menuDTOList.stream()
                    .anyMatch(menu -> requestURI.contains(menu.getMenuGroup().getGroupStartUrl()));

            if (isAllowed) {
                // 허용된 시작 URL 중 하나에 해당하는 경우 요청 계속 진행
                chain.doFilter(request, response);
            } else {
                throw new NotAllowedRequestException("해당 요청에 대한 권한이 없습니다.");

            }
        } else { // 요청 uri가 권한이 필요하지 않는 uri이면 필터 통과
            chain.doFilter(request, response);

        }

    }

    private boolean isAllowedStartUrl(String requestURI) {
        return allowedStartUrls.stream().anyMatch(url -> requestURI.contains(url));   // /api/member/all
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
