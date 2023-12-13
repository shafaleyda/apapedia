package com.apapedia.order.service;

import com.apapedia.order.model.CartModel;
import java.util.UUID;
import java.io.IOException;
import java.util.List;

public interface CartService {
    CartModel createCart(CartModel cart);

    CartModel getCartById(UUID id);

    List<CartModel> getCartByUserId(UUID userId);

    CartModel updateCart(CartModel cart) throws IOException, InterruptedException;
}
