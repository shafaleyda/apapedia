package com.apapedia.order.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.apapedia.order.dto.CreateCartRequestDTO;
import com.apapedia.order.model.CartModel;
import com.apapedia.order.dto.CartMapper;

import com.apapedia.order.service.CartService;

@RestController
@RequestMapping("/api")
public class CartRestController {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartService cartService;

    @PostMapping(value = "/cart/create")
    private CartModel createCart(@Valid @RequestBody CreateCartRequestDTO cartRequestDTO){
        CartModel cartModel = cartMapper.createCartRequestDTOToCartModel(cartRequestDTO);
        return cartService.createCart(cartModel);
    }
}