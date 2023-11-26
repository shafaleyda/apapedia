package com.apapedia.order.dto.response;

import java.util.Set;

import com.apapedia.order.model.OrderModel;
import com.apapedia.order.model.OrderItemModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private OrderModel order;

    private Set<OrderItemModel> listOrderItem;
}
