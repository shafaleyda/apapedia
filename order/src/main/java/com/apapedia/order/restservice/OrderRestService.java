package com.apapedia.order.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.order.model.OrderModel;

public interface OrderRestService {
    void saveOrder(OrderModel order);
    
    List<OrderModel> retrieveRestAllOrder();

    OrderModel getRestOrderByOrderId(UUID id);

    OrderModel updateRestOrder(OrderModel order, OrderModel oldOrder);

    List<OrderModel> listByCustomer(UUID customer);

    List<OrderModel> listBySeller(UUID seller);

    

}
