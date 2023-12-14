package com.apapedia.frontend.config;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

// menangkap token otentikasi dari cookie (jwtToken) setiap request API dipanggil

@Slf4j
public class InjectToken implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            Cookie jwtTokenCookie = WebUtils.getCookie(attributes.getRequest(), "jwtToken");
            if (jwtTokenCookie != null && StringUtils.hasText(jwtTokenCookie.getValue())) {
                String token = jwtTokenCookie.getValue();
                request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                log.info("Header autorisasi sudah keset dengan token: Bearer {}", token);
            } else {
                log.warn("Token JWT tida valid nih");
            }
        }

        return execution.execute(request, body);
    }
}
