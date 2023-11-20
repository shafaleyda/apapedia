package com.apapedia.order.service;

import com.apapedia.order.repository.OrderItemDb;
import com.apapedia.order.model.OrderItemModel;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemServiceImpl implements OrderItemService{
    @Autowired
    OrderItemDb orderItemDb;

    @Override
    public OrderItemModel createOrderItem(OrderItemModel orderItem){
        return orderItemDb.save(orderItem);
    }

    @Override
    public List<OrderItemModel> getOrderItemByOrderId(UUID id){
        return orderItemDb.findByOrderId(id);
    }
}