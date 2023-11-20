package com.apapedia.order.service;

import java.util.UUID;

import com.apapedia.order.rest.ProductRest;

import reactor.core.publisher.Mono;

public interface ProductMockService {
    Mono<ProductRest> getProductById(UUID id);
}
