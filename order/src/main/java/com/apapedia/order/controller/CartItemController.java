package com.apapedia.order.controller;

import java.util.UUID;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.apapedia.order.model.CartItemModel;
import com.apapedia.order.model.CartModel;
import com.apapedia.order.dto.CartItemMapper;
import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.dto.request.DeleteCartItemDTO;
import com.apapedia.order.dto.request.UpdateCartItemRequestDTO;
import com.apapedia.order.service.CartItemService;
import com.apapedia.order.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.apapedia.order.dto.response.CartItemRest;
import com.apapedia.order.dto.response.Catalogue;

@RestController
@CrossOrigin
public class CartItemController {
    @Autowired
    CartItemMapper cartItemMapper;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    CartService cartService;

    String baseUrlCatalogue = "http://localhost:8082";

    @PostMapping(value = "/cart/{id}/add")
    private CartItemModel createCartItem(@Valid @RequestBody CreateCartItemRequestDTO cartItemRequestDTO, @PathVariable(value = "id") UUID id) throws IOException, InterruptedException{
        CartItemModel cartItemModel = cartItemMapper.createCartItemRequestDTOToCartItemModel(cartItemRequestDTO);
        CartModel cartModel = cartService.getCartById(id);
        cartItemModel.setCart(cartModel);
        cartItemModel = cartItemService.createCartItem(cartItemModel);
        cartModel.getListCartItem().add(cartItemModel);
        cartService.updateCart(cartModel);
        return cartItemModel;
    }

    @PutMapping(value = "/cart/{id}/update")
    private CartItemModel updateCartItem(@Valid @RequestBody UpdateCartItemRequestDTO cartItemRequestDTO, @PathVariable(value = "id") UUID cartId) throws IOException, InterruptedException{
        CartItemModel cartItemModel = cartItemMapper.updateCartItemRequestDTOToCartItemModel(cartItemRequestDTO);
        CartModel cartModel = cartService.getCartById(cartId);
        CartItemModel cartItemModelToUpdate = cartItemService.getCartItemById(cartItemModel.getId());
        cartItemModelToUpdate.setQuantity(cartItemModel.getQuantity());
        cartItemModelToUpdate = cartItemService.updateCartItem(cartItemModelToUpdate);
        cartService.updateCart(cartModel);
        return cartItemModelToUpdate;
    }

    @GetMapping(value = "cart/customer/{user_id}")
    private List<CartItemRest> getCartByUserId(@PathVariable(value = "user_id") UUID userId) throws IOException, InterruptedException{
        var cart = cartService.getCartByUserId(userId);
        List<CartItemModel> cartItems = cart.get(0).getListCartItem();
        List<CartItemRest> cartItemRests = new ArrayList<CartItemRest>();
        for (CartItemModel cartItemModel : cartItems) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrlCatalogue + "/api/catalog/" + cartItemModel.getProductId()))
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            Catalogue catalog = objectMapper.readValue(responseBody, Catalogue.class);

            CartItemRest cartItemRest = new CartItemRest();
            cartItemRest.setId(cartItemModel.getId());
            cartItemRest.setProductId(cartItemModel.getProductId());
            cartItemRest.setSeller(catalog.getSeller());
            cartItemRest.setPrice(catalog.getPrice());
            cartItemRest.setProductName(catalog.getProductName());
            cartItemRest.setProductDescription(catalog.getProductDescription());
            cartItemRest.setCategoryId(catalog.getCategoryId());
            cartItemRest.setCategoryName(catalog.getCategoryName());
            cartItemRest.setStock(catalog.getStock());
            cartItemRest.setImage(catalog.getImage());
            cartItemRest.setIsDeleted(catalog.isDeleted());
            cartItemRest.setQuantity(cartItemModel.getQuantity());
            cartItemRests.add(cartItemRest);
        }
        return cartItemRests;
    }

    @DeleteMapping(value = "/cart/{id}/delete")
    private void deleteCartItem(@PathVariable(value = "id") UUID id, @RequestBody DeleteCartItemDTO deleteCartItemDTO) throws IOException, InterruptedException{
        CartModel cartModel = cartService.getCartById(id);
        CartItemModel cartItemModel = cartItemService.getCartItemById(deleteCartItemDTO.getCartItemId());
        cartModel.getListCartItem().remove(cartItemModel);
        cartService.updateCart(cartModel);
        cartItemService.deleteCartItem(cartItemModel);
    }
}
