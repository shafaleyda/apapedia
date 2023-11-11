package com.apapedia.order.restcontroller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.order.dto.OrderMapper;
import com.apapedia.order.dto.request.UpdateOrderRequestDTO;
import com.apapedia.order.model.OrderModel;
import com.apapedia.order.restservice.OrderRestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    private OrderRestService orderRestService;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping(value = "/order/customer/{id}")
    private List<OrderModel> retrieveCustomerOrder(@PathVariable("id") UUID id){
        List<OrderModel> listOrder = orderRestService.listByCustomer(id);
        return listOrder;
    }

    @GetMapping(value = "/order/seller/{id}")
    private List<OrderModel> retrieveSellerOrder(@PathVariable("id") UUID id){
        List<OrderModel> listOrder = orderRestService.listBySeller(id);
        return listOrder;
    }

    @PutMapping("/{orderId}/status")
    public OrderModel updateOrderStatus(
            @Valid @RequestBody UpdateOrderRequestDTO orderDTO, BindingResult bindingResult) {

        if(bindingResult.hasFieldErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"); 
        } else {
            var order = orderMapper.updateOrderRequestDTOToOrder(orderDTO);
            OrderModel orderUpdated = orderRestService.updateRestOrder(order); 
            
            return orderUpdated; 
        }
    }

}
