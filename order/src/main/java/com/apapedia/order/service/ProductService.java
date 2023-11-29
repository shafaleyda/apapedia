package com.apapedia.order.service;

import java.util.UUID;

import com.apapedia.order.dto.response.Product;

import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> getProductById(UUID id);
}
