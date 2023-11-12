package com.apapedia.order.dto;

import org.mapstruct.Mapper;

import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.dto.request.UpdateOrderRequestDTO;
import com.apapedia.order.model.OrderModel;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderModel createOrderRequestDTOToOrder(CreateOrderRequestDTO createOrderRequestDTO);

    OrderModel updateOrderRequestDTOToOrder(UpdateOrderRequestDTO updateBukuRequestDTO);
}
