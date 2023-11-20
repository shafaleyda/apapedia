package com.apapedia.order.service;

import com.apapedia.order.model.OrderItemModel;

import java.util.List;
import java.util.UUID;

public interface OrderItemService {
    OrderItemModel createOrderItem(OrderItemModel orderItem);

    List<OrderItemModel> getOrderItemByOrderId(UUID id);
}
