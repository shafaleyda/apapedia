package com.apapedia.order.dto;

import org.mapstruct.Mapper;

import com.apapedia.order.dto.request.UpdateOrderRequestDTO;
import com.apapedia.order.model.OrderModel;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderModel updateOrderRequestDTOToOrder(UpdateOrderRequestDTO updateBukuRequestDTO);
}
