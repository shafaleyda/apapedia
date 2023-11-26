package com.apapedia.order.dto;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.dto.request.UpdateCartItemRequestDTO;
import com.apapedia.order.model.CartItemModel;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemModel createCartItemRequestDTOToCartItemModel(CreateCartItemRequestDTO createCartRequestDTO);
    @AfterMapping
    default void setProductId(CreateCartItemRequestDTO createCartRequestDTO, @MappingTarget CartItemModel cartModel){
        cartModel.setProductId(createCartRequestDTO.getProductId());
        cartModel.setQuantity(createCartRequestDTO.getQuantity());
    }

    CartItemModel updateCartItemRequestDTOToCartItemModel(UpdateCartItemRequestDTO updateCartRequestDTO);
    @AfterMapping
    default void setId(UpdateCartItemRequestDTO updateCartRequestDTO, @MappingTarget CartItemModel cartModel){
        cartModel.setId(updateCartRequestDTO.getId());
    }
}
