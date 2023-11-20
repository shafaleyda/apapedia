package com.apapedia.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.apapedia.order.model.OrderItemModel;
import com.apapedia.order.model.OrderModel;
import com.apapedia.order.rest.ProductRest;
import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.service.OrderItemService;
import com.apapedia.order.service.OrderService;
import com.apapedia.order.service.ProductMockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.UUID;
import java.time.LocalDateTime;

@RestController
public class OrderController {
    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductMockService productMockService;

    @PostMapping(value = "/order/create")
    private List<OrderModel> createOrder(@Valid @RequestBody CreateOrderRequestDTO orderRequestDTO){
        List<OrderModel> orderModels = new ArrayList<>();
        System.out.println(orderRequestDTO.getItems().size());
        for(Map.Entry<UUID,Integer> entry : orderRequestDTO.getItems().entrySet()){
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();
            //Use mock service to get product price
            ProductRest product = productMockService.getProductById(productId).block();

            //Use this to check if automatically create new order model
            product.setSeller(UUID.randomUUID());

            //Check if order already exist
            OrderModel order = null;
            if(orderModels.size() > 0){
                for(OrderModel existingOrder : orderModels){
                    if(existingOrder.getSeller().equals(product.getSeller())){
                        order = existingOrder;
                        break;
                    }
                }
            }
            
            //Create order item
            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setProductId(productId);
            orderItem.setQuantity(quantity);
            orderItem.setProductPrice(product.getPrice());
            orderItem.setProductName(product.getName());

            //Create order
            if (order != null) {
                order.getListOrderItem().add(orderItem);
                order.setTotalPrice(order.getTotalPrice() + product.getPrice() * quantity);
            } else{
                order = new OrderModel();
                order.setSeller(product.getSeller());
                order.setCustomer(orderRequestDTO.getCustomer());
                order.setCreatedAt(LocalDateTime.now());
                order.setUpdatedAt(LocalDateTime.now());
                order.setTotalPrice(product.getPrice() * quantity);
                order.setStatus(0);
                orderService.createOrder(order);
                order.setListOrderItem(new HashSet<OrderItemModel>());
                order.getListOrderItem().add(orderItem);
                orderModels.add(order);
            }
            orderItem.setOrder(order);
            orderItemService.createOrderItem(orderItem);
            orderService.createOrder(order);
        }
        return orderModels;
    }
}
