package com.apapedia.order.service;

import com.apapedia.order.model.CartItemModel;
import java.util.UUID;

public interface CartItemService {
    CartItemModel createCartItem(CartItemModel cart);

    CartItemModel getCartItemById(UUID id);

    CartItemModel updateCartItem(CartItemModel cart);

    void deleteCartItem(CartItemModel cart);
}
