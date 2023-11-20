package com.apapedia.order.service;

import com.apapedia.order.model.OrderModel;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    void saveOrder(OrderModel order);
    
    List<OrderModel> retrieveAllOrder();

    OrderModel getOrderByOrderId(UUID id);

    OrderModel updateOrder(OrderModel order, OrderModel oldOrder);

    List<OrderModel> listByCustomer(UUID customer);

    List<OrderModel> listBySeller(UUID seller);
}
