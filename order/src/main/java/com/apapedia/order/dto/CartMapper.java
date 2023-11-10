package com.apapedia.order.dto;

import org.mapstruct.Mapper;

import com.apapedia.order.model.CartModel;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartModel createCartRequestDTOToCartModel(CreateCartRequestDTO createCartRequestDTO);
}
