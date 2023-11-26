package com.apapedia.order.controller;

import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.apapedia.order.model.CartItemModel;
import com.apapedia.order.model.CartModel;
import com.apapedia.order.dto.CartItemMapper;
import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.dto.request.DeleteCartItemDTO;
import com.apapedia.order.dto.request.UpdateCartItemRequestDTO;
import com.apapedia.order.service.CartItemService;
import com.apapedia.order.service.CartService;
import com.apapedia.order.service.ProductService;

@RestController
public class CartItemController {
    @Autowired
    CartItemMapper cartItemMapper;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productMockService;

    @PostMapping(value = "/cart/{id}/add")
    private CartItemModel createCartItem(@Valid @RequestBody CreateCartItemRequestDTO cartItemRequestDTO, @PathVariable(value = "id") UUID id){
        CartItemModel cartItemModel = cartItemMapper.createCartItemRequestDTOToCartItemModel(cartItemRequestDTO);
        CartModel cartModel = cartService.getCartById(id);
        cartItemModel.setCart(cartModel);
        cartItemModel = cartItemService.createCartItem(cartItemModel);
        cartModel.getListCartItem().add(cartItemModel);
        cartService.updateCart(cartModel);
        return cartItemModel;
    }

}
