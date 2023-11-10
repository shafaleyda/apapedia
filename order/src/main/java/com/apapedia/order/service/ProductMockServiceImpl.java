package com.apapedia.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.order.rest.ProductRest;

import reactor.core.publisher.Mono;

@Service
public class ProductMockServiceImpl implements ProductMockService{

    private final String mockUrl = "https://51248524-ac33-4f52-8a3f-e63858a3ed99.mock.pstmn.io";

    private final WebClient webClient;

    public ProductMockServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl(mockUrl).build();
    }

    @Override
    public Mono<ProductRest> getProductById(UUID id){
        var response = this.webClient.get().uri("api/product/{id}", id).retrieve().bodyToMono(ProductRest.class);
        return response;
    }
}
