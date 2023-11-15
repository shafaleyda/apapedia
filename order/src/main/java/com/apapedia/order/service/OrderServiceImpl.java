package com.apapedia.order.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.apapedia.order.repository.OrderDb;

import jakarta.transaction.Transactional;

import com.apapedia.order.model.OrderModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderDb orderDb;

    @Override
    public void saveOrder(OrderModel order){
        orderDb.save(order);
    }

    @Override
    public List<OrderModel> retrieveAllOrder(){
        return orderDb.findAll();
    }

    @Override
    public List<OrderModel> listByCustomer(UUID customer) {
        return orderDb.findAllByCustomer(customer);
    }

    @Override
    public List<OrderModel> listBySeller(UUID seller) {
        return orderDb.findAllBySeller(seller);
    }

    @Override
    public OrderModel getOrderByOrderId(UUID id){
        for (OrderModel order: retrieveAllOrder()){
            if (order.getId().equals(id)){
                return order;
            }
        } return null;
    }

    @Override
    public OrderModel updateOrder(OrderModel orderFromDTO, OrderModel oldOrder){
        OrderModel order = getOrderByOrderId(orderFromDTO.getId());
        if (order != null){
            order.setUpdatedAt(LocalDateTime.now());
            order.setStatus(orderFromDTO.getStatus());
            order.setCustomer(oldOrder.getCustomer());

            //new
            order.setCreatedAt(oldOrder.getCreatedAt());
            order.setTotalPrice(oldOrder.getTotalPrice());
            order.setSeller(oldOrder.getSeller());
            orderDb.save(order);
        }
        return order;
    }
}
