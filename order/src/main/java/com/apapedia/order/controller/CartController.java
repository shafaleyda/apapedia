package com.apapedia.order.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.apapedia.order.dto.CartMapper;
import com.apapedia.order.dto.request.CreateCartRequestDTO;
import com.apapedia.order.model.CartModel;
import com.apapedia.order.service.CartService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin
public class CartController {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartService cartService;

    @PostMapping(value = "/cart/create")
    private UUID createCart(@Valid @RequestBody CreateCartRequestDTO cartRequestDTO){
        CartModel cartModel = cartMapper.createCartRequestDTOToCartModel(cartRequestDTO);
        return cartService.createCart(cartModel).getId();
    }

    @GetMapping("/cart/{id}")
    private CartModel getCartById(@PathVariable(value = "id") String id){
        return cartService.getCartById(UUID.fromString(id));
    }

    @GetMapping("/cart/user/{id}")
    private List<CartModel> getCartByUserId(@PathVariable(value = "id") String id){
        return cartService.getCartByUserId(UUID.fromString(id));
    }
    
}
