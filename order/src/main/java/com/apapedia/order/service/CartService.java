package com.apapedia.order.service;

import com.apapedia.order.model.CartModel;
import java.util.UUID;
import java.io.IOException;
import java.util.Optional;

public interface CartService {
    CartModel createCart(CartModel cart);

    CartModel getCartById(UUID id);

    Optional<CartModel> getCartByUserId(UUID userId);
    UUID getCartByIdUser(UUID idUser); 
    CartModel updateCart(CartModel cart) throws IOException, InterruptedException;
}
