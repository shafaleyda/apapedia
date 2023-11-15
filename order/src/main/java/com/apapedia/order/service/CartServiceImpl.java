package com.apapedia.order.service;

import com.apapedia.order.repository.CartDb;
import com.apapedia.order.model.CartModel;
import com.apapedia.order.dto.response.Product;
import com.apapedia.order.model.CartItemModel;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartDb cartDb;

    @Autowired
    ProductService productMockService;

    @Override
    public CartModel createCart(CartModel cart){
        return cartDb.save(cart);
    }

    @Override
    public CartModel getCartById(UUID id){
        return cartDb.findById(id).get();
    }

    @Override
    public Optional<CartModel> getCartByUserId(UUID userId){
        return cartDb.findByUserId(userId);
    }

    @Override
    public CartModel updateCart(CartModel cart){
        cart.setTotalPrice(0);
        for(CartItemModel cartItem : cart.getListCartItem()){
            Product product = productMockService.getProductById(cartItem.getProductId()).block();
            cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * cartItem.getQuantity()));
        }
        return cartDb.save(cart);
    }
}
