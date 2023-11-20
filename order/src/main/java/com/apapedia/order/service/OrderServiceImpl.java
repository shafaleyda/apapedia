package com.apapedia.order.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.apapedia.order.repository.OrderDb;

import jakarta.transaction.Transactional;

import com.apapedia.order.model.OrderModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        System.out.println(order);
        System.out.println(orderFromDTO);
        if (order != null){
            order.setUpdatedAt(LocalDateTime.now());
            order.setStatus(orderFromDTO.getStatus());
            order.setCustomer(oldOrder.getCustomer());
            order.setListOrderItem(oldOrder.getListOrderItem());
            order.setCreatedAt(oldOrder.getCreatedAt());
            order.setTotalPrice(oldOrder.getTotalPrice());
            order.setSeller(oldOrder.getSeller());
            orderDb.save(order);
        }
        return order;
    }

    @Override
    public Map<LocalDate, Integer> getDailySales(UUID seller){
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1);
        
        List<OrderModel> salesForMonth = orderDb.findByCreatedAtBetweenAndSeller(
                firstDayOfMonth,
                now,
                seller
        );

        Map<LocalDate, Integer> salesPerDayMap = new HashMap<>();
        LocalDate currentDay = firstDayOfMonth.toLocalDate();
        LocalDate lastDayOfMonth = now.toLocalDate();

        while (!currentDay.isAfter(lastDayOfMonth)) {
            salesPerDayMap.put(currentDay, 0);
            currentDay = currentDay.plusDays(1);
        }

        for (OrderModel order : salesForMonth) {
            LocalDateTime orderDate = order.getCreatedAt();
            LocalDate date = orderDate.toLocalDate();
            salesPerDayMap.merge(date, 1, Integer::sum);
        }

        return salesPerDayMap;

    }
}
