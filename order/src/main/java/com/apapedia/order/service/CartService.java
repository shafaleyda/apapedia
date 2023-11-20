package com.apapedia.order.service;

import com.apapedia.order.model.CartModel;
import java.util.UUID;

public interface CartService {
    CartModel createCart(CartModel cart);

    CartModel getCartById(UUID id);

    CartModel updateCart(CartModel cart);
}
