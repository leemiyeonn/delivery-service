package org.delivery.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 요청 URL을 로그에 기록
        log.info("Authorization Interceptor url : {}", request.getRequestURI());

        // CORS 사전 요청 (Preflight request) 처리
        if(HttpMethod.OPTIONS.matches(request.getMethod())){
            return true; // 사전 요청인 경우, 인터셉터를 통과시킴
        }

        // 정적 리소스 요청 처리
        if(handler instanceof ResourceHttpRequestHandler){
            return true; // 정적 리소스 요청인 경우, 인터셉터를 통과시킴
        }

        // Todo token 검증 기능 구현

        // Todo 인증 실패시 오류 처리 구현

        return true;
    }
}
