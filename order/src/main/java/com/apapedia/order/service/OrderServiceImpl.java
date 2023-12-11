package com.apapedia.order.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;

import com.apapedia.order.repository.OrderDb;
import com.apapedia.order.repository.OrderItemDb;

import jakarta.transaction.Transactional;

import com.apapedia.order.dto.response.Order;
import com.apapedia.order.model.OrderItemModel;
import com.apapedia.order.model.OrderModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderDb orderDb;

    @Autowired
    OrderItemDb orderItemDb;

    @Override
    public void saveOrder(OrderModel order){
        orderDb.save(order);
    }

    @Override
    public List<OrderModel> retrieveAllOrder(){
        return orderDb.findAll();
    }

    @Override
    public List<Order> listByCustomer(UUID customer) {
        // return orderDb.findAllByCustomer(customer);
        List<OrderModel> orderModels = orderDb.findAllByCustomer(customer);
        return orderModels.stream()
                .map(orderModel -> {
                    Order orderDto = new Order();
                    orderDto.setOrder(orderModel);
                    orderDto.setListOrderItem(orderModel.getListOrderItem());
                    return orderDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> listBySeller(UUID seller) {
        List<OrderModel> orderModels = orderDb.findAllBySeller(seller);
        return orderModels.stream()
                .map(orderModel -> {
                    Order orderDto = new Order();
                    orderDto.setOrder(orderModel);
                    orderDto.setListOrderItem(orderModel.getListOrderItem());
                    return orderDto;
                })
                .collect(Collectors.toList());
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
        System.out.println(order.getStatus());
        System.out.println(orderFromDTO.getStatus());
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
    
            int quantitySold = order.getListOrderItem().stream()
                    .mapToInt(OrderItemModel::getQuantity)
                    .sum();
    
            salesPerDayMap.merge(date, quantitySold, Integer::sum);
        }

        return salesPerDayMap;

    }

    public List<OrderItemModel> getListOrderItemById(UUID orderId){
        return orderItemDb.findByOrderId(orderId);
    }

    public Boolean getWithdrawnById(UUID id){
        return getOrderByOrderId(id).isWithdrawn();
    }
}
