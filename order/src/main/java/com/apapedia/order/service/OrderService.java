package com.apapedia.order.service;

import com.apapedia.order.dto.response.Order;
import com.apapedia.order.model.OrderItemModel;
import com.apapedia.order.model.OrderModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderService {
    void saveOrder(OrderModel order);
    
    List<OrderModel> retrieveAllOrder();

    OrderModel getOrderByOrderId(UUID id);

    OrderModel updateOrder(OrderModel order, OrderModel oldOrder);

    List<Order> listByCustomer(UUID customer);

    List<Order> listBySeller(UUID seller);

    Map<LocalDate, Integer> getDailySales(UUID seller);

    List<OrderItemModel> getListOrderItemById(UUID orderId);

    Boolean getWithdrawnById(UUID id);
}
