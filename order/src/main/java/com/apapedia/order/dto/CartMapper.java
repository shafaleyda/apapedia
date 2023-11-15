package com.apapedia.order.dto;

import org.mapstruct.Mapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import com.apapedia.order.dto.request.CreateCartRequestDTO;
import com.apapedia.order.model.CartModel;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartModel createCartRequestDTOToCartModel(CreateCartRequestDTO createCartRequestDTO);
    @AfterMapping
    default void setUserId(CreateCartRequestDTO createCartRequestDTO, @MappingTarget CartModel cartModel){
        cartModel.setUserId(createCartRequestDTO.getUserId());
    }
}
