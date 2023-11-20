package com.apapedia.order.service;

import com.apapedia.order.model.OrderModel;
import java.util.UUID;

public interface OrderService {
    OrderModel createOrder(OrderModel order);

    OrderModel getOrderById(UUID id);

    OrderModel updateOrder(OrderModel order);
}
