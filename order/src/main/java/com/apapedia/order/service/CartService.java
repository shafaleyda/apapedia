package com.apapedia.order.service;

import com.apapedia.order.model.CartModel;
import java.util.UUID;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CartService {
    CartModel createCart(CartModel cart);

    CartModel getCartById(UUID id);

    Optional<CartModel> getCartByUserId(UUID userId);

    CartModel updateCart(CartModel cart) throws IOException, InterruptedException;
}
