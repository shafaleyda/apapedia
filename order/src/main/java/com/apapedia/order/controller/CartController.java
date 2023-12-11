package com.apapedia.order.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.apapedia.order.dto.CartMapper;
import com.apapedia.order.dto.request.CreateCartRequestDTO;
import com.apapedia.order.model.CartModel;
import com.apapedia.order.service.CartService;

import java.util.UUID; 

@CrossOrigin
@RestController
public class CartController {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartService cartService;

    @PostMapping(value = "/cart/create")
    private CartModel createCart(@Valid @RequestBody CreateCartRequestDTO cartRequestDTO){
        CartModel cartModel = cartMapper.createCartRequestDTOToCartModel(cartRequestDTO);
        return cartService.createCart(cartModel);
    }

    @GetMapping(value = "/cart/get-user/{idUser}")
    private UUID getCartFromUser(@PathVariable(value = "idUser") UUID userId) {
        return cartService.getCartByIdUser(userId); 
    }
}
