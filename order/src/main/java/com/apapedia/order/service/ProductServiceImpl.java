package com.apapedia.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.order.dto.response.Product;

import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService{

    private final String mockUrl = "https://51248524-ac33-4f52-8a3f-e63858a3ed99.mock.pstmn.io";

    private final WebClient webClient;

    public ProductServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl(mockUrl).build();
    }

    @Override
    public Mono<Product> getProductById(UUID id){
        var response = this.webClient.get().uri("api/product/{id}", id).retrieve().bodyToMono(Product.class);
        return response;
    }
}
