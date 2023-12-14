//package com.apapedia.frontend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class RestTemplateConfig {
//
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplateProfile = new RestTemplate();
//
//        List<ClientHttpRequestInterceptor> interceptors = restTemplateProfile.getInterceptors();
//        if (CollectionUtils.isEmpty(interceptors)) {
//            interceptors = new ArrayList<>();
//        }
//
//        interceptors.add(new InjectToken());
//
//        restTemplateProfile.setInterceptors(interceptors);
//        return restTemplateProfile;
//    }
//
//}
