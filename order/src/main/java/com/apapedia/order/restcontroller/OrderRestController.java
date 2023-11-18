package com.apapedia.order.restcontroller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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

    @PutMapping("/order/update/{orderId}")
    public OrderModel updateOrderStatus(@PathVariable("orderId") UUID id,
            @Valid @RequestBody UpdateOrderRequestDTO orderDTO, BindingResult bindingResult) {
    
        System.out.println("Received request for orderId: " + id);
        OrderModel oldOrder = orderRestService.getRestOrderByOrderId(id);
    
        if(bindingResult.hasFieldErrors()){
            System.out.println("Invalid request body: " + bindingResult.getAllErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"); 
        } else {
            var order = orderMapper.updateOrderRequestDTOToOrder(orderDTO);
            order.setId(id);
            OrderModel orderUpdated = orderRestService.updateRestOrder(order, oldOrder); 
            
            System.out.println("Order updated successfully: " + orderUpdated);
            return orderUpdated; 
        }
    }
}
