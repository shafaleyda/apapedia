package com.apapedia.order.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import com.apapedia.order.model.CartItemModel;
import com.apapedia.order.model.CartModel;
import com.apapedia.order.dto.CartItemMapper;
import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.service.CartItemService;
import com.apapedia.order.service.CartService;
import com.apapedia.order.service.ProductMockService;
import com.apapedia.order.rest.ProductRest;

@RestController
public class CartItemController {
    @Autowired
    CartItemMapper cartItemMapper;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    CartService cartService;

    @Autowired
    ProductMockService productMockService;

    @PostMapping(value = "/cart/{id}/add")
    private CartItemModel createCartItem(@Valid @RequestBody CreateCartItemRequestDTO cartItemRequestDTO, @PathVariable(value = "id") UUID id){
        CartItemModel cartItemModel = cartItemMapper.createCartItemRequestDTOToCartItemModel(cartItemRequestDTO);
        CartModel cartModel = cartService.getCartById(id);
        cartItemModel.setCart(cartModel);
        cartItemModel = cartItemService.createCartItem(cartItemModel);

        //Use mock service to get product price
        Mono<ProductRest> product = productMockService.getProductById(cartItemRequestDTO.getProductId());
        
        cartModel.getListCartItem().add(cartItemModel);
        cartModel.setTotalPrice(cartModel.getTotalPrice() + (product.block().getPrice() * cartItemRequestDTO.getQuantity()));
        cartService.updateCart(cartModel);
        return cartItemModel;
    }
}
