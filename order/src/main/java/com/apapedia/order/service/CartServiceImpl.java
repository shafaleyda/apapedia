package com.apapedia.order.service;

import com.apapedia.order.repository.CartDb;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.apapedia.order.model.CartModel;
import com.apapedia.order.dto.response.Catalogue;
import com.apapedia.order.model.CartItemModel;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartDb cartDb;

    String baseUrlCatalogue = "http://localhost:8082";

    @Override
    public CartModel createCart(CartModel cart){
        return cartDb.save(cart);
    }

    @Override
    public CartModel getCartById(UUID id){
        return cartDb.findById(id).get();
    }

    @Override
    public List<CartModel> getCartByUserId(UUID userId){
        return cartDb.findByUserId(userId);
    }

    @Override
    public CartModel updateCart(CartModel cart) throws IOException, InterruptedException{
        cart.setTotalPrice(0);
        for(CartItemModel cartItem : cart.getListCartItem()){
            HttpRequest requestProduct = HttpRequest.newBuilder()
                .uri(URI.create(baseUrlCatalogue + "/api/catalog/" + cartItem.getProductId()))
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<String> responseProduct = HttpClient.newHttpClient().send(requestProduct, HttpResponse.BodyHandlers.ofString());
            String responseBodyProduct = responseProduct.body();
            ObjectMapper objectMapperProduct = new ObjectMapper();
            Catalogue product = objectMapperProduct.readValue(responseBodyProduct, Catalogue.class);
            cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * cartItem.getQuantity()));
        }
        return cartDb.save(cart);
    }
}
