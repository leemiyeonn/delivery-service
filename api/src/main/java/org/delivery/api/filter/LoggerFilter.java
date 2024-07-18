package org.delivery.api.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 요청과 응답을 래핑하여 여러 번 읽을 수 있게 함
        var req = new ContentCachingRequestWrapper((HttpServletRequest) request);
        var res = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // 초기 URI 로깅 (이 시점에는 요청 본문이 아직 읽히지 않음)
        log.info("INIT URI : {}", req.getRequestURI());

        // 다음 필터 또는 실제 요청 처리기로 요청을 전달
        chain.doFilter(req, res);

        // 요청 처리 후 로깅 시작

        // 요청 헤더 정보 수집
        var headerNames = req.getHeaderNames();
        var headerValues = new StringBuilder();

        headerNames.asIterator().forEachRemaining(headerKey -> {
            var headerValue = req.getHeader(headerKey);
            // 각 헤더를 [키 : 값] 형식으로 추가
            headerValues.append("[").append(headerKey).append(" : ")
                    .append(headerValue).append("] ");
        });

        // 요청 본문 읽기 (ContentCachingRequestWrapper 덕분에 가능)
        var requestBody = new String(req.getContentAsByteArray());
        var uri = req.getRequestURI();  // 요청 URI 가져오기
        var method = req.getMethod();   // HTTP 메소드 가져오기

        // 요청 정보 로깅
        log.info(">>>>> uri : {} , method : {}, header : {} , body : {}",
                uri, method, headerValues, requestBody);

        // 응답 헤더 정보 수집
        var responseHeaderValues = new StringBuilder();

        res.getHeaderNames().forEach(headerKey -> {
            var headerValue = res.getHeader(headerKey);
            // 각 응답 헤더를 [키 : 값] 형식으로 추가
            responseHeaderValues.append("[").append(headerKey).append(" : ")
                    .append(headerValue).append("] ");
        });

        // 응답 본문 읽기 (ContentCachingResponseWrapper 덕분에 가능)
        var responseBody = new String(res.getContentAsByteArray());

        // 응답 정보 로깅
        log.info("<<<<< uri : {} , method : {}, header : {} , body : {}",
                uri, method, responseHeaderValues, responseBody);

        // 응답 본문을 클라이언트에게 전송
        // (ContentCachingResponseWrapper를 사용했기 때문에 필수)
        res.copyBodyToResponse();
    }
}