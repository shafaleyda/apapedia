package com.apapedia.order.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.apapedia.order.repository.OrderDb;
import com.apapedia.order.model.OrderModel;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderDb orderDb;

    @Override
    public OrderModel createOrder(OrderModel order){
        return orderDb.save(order);
    }

    @Override
    public OrderModel getOrderById(UUID id){
        return null;
    }

    @Override
    public OrderModel updateOrder(OrderModel order){
        return null;
    }
}
