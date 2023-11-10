package com.apapedia.order.service;

import com.apapedia.order.repository.CartDb;
import com.apapedia.order.model.CartModel;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartDb cartDb;

    @Override
    public CartModel createCart(CartModel cart){
        return cartDb.save(cart);
    }

    @Override
    public CartModel getCartById(UUID id){
        return cartDb.findById(id).get();
    }

    @Override
    public CartModel updateCart(CartModel cart){
        return cartDb.save(cart);
    }
}
