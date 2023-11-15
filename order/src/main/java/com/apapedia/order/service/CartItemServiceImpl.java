package com.apapedia.order.service;

import com.apapedia.order.repository.CartItemDb;
import com.apapedia.order.model.CartItemModel;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Service
public class CartItemServiceImpl implements CartItemService{
    @Autowired
    CartItemDb cartItemDb;

    @Override
    public CartItemModel createCartItem(CartItemModel cartItem){
        return cartItemDb.save(cartItem);
    }

    @Override
    public CartItemModel getCartItemById(UUID id){
        return cartItemDb.findById(id).get();
    }

    @Override
    public CartItemModel updateCartItem(CartItemModel cartItem){
        return cartItemDb.save(cartItem);
    }

    @Override
    public void deleteCartItem(CartItemModel cartItem){
        cartItemDb.delete(cartItem);
    }
}
