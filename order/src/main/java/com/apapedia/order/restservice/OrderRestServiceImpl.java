package com.apapedia.order.restservice;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.order.model.OrderModel;
import com.apapedia.order.repository.OrderDb;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderRestServiceImpl implements OrderRestService{
    @Autowired
    private OrderDb orderDb;

    @Override
    public void saveOrder(OrderModel order){
        orderDb.save(order);
    }

    @Override
    public List<OrderModel> retrieveRestAllOrder(){
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
    public OrderModel getRestOrderByOrderId(UUID id){
        for (OrderModel order: retrieveRestAllOrder()){
            if (order.getId().equals(id)){
                return order;
            }
        } return null;
    }

    @Override
    public OrderModel updateRestOrder(OrderModel orderFromDTO, OrderModel oldOrder){
        OrderModel order = getRestOrderByOrderId(orderFromDTO.getId());
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
