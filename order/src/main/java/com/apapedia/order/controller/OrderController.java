package com.apapedia.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.apapedia.order.dto.OrderMapper;
import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.dto.request.UpdateOrderRequestDTO;
import com.apapedia.order.dto.response.Order;
import com.apapedia.order.dto.response.Product;
import com.apapedia.order.model.OrderItemModel;
import com.apapedia.order.model.OrderModel;
import com.apapedia.order.service.OrderItemService;
import com.apapedia.order.service.OrderService;
import com.apapedia.order.service.ProductService;

import io.netty.handler.codec.http.HttpObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.Hashtable;

@RestController
public class OrderController {
    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ProductService productMockService;

    @PostMapping(value = "/order/create")
    private List<OrderModel> createOrder(@Valid @RequestBody CreateOrderRequestDTO orderRequestDTO){
        List<OrderModel> orderModels = new ArrayList<>();
        System.out.println(orderRequestDTO.getItems().size());
        for(Map.Entry<UUID,Integer> entry : orderRequestDTO.getItems().entrySet()){
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();
            //Use mock service to get product price
            Product product = productMockService.getProductById(productId).block();

            //Use this to check if automatically create new order model
            product.setSeller(UUID.randomUUID());

            //Check if order already exist
            OrderModel order = null;
            if(orderModels.size() > 0){
                for(OrderModel existingOrder : orderModels){
                    if(existingOrder.getSeller().equals(product.getSeller())){
                        order = existingOrder;
                        break;
                    }
                }
            }
            
            //Create order item
            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setProductId(productId);
            orderItem.setQuantity(quantity);
            orderItem.setProductPrice(product.getPrice());
            orderItem.setProductName(product.getName());

            //Create order
            if (order != null) {
                order.getListOrderItem().add(orderItem);
                order.setTotalPrice(order.getTotalPrice() + product.getPrice() * quantity);
            } else{
                order = new OrderModel();
                order.setSeller(product.getSeller());
                order.setCustomer(orderRequestDTO.getCustomer());
                order.setCreatedAt(LocalDateTime.now());
                order.setUpdatedAt(LocalDateTime.now());
                order.setTotalPrice(product.getPrice() * quantity);
                order.setStatus(0);
                orderService.saveOrder(order);
                order.setListOrderItem(new HashSet<OrderItemModel>());
                order.getListOrderItem().add(orderItem);
                orderModels.add(order);
            }
            orderItem.setOrder(order);
            orderItemService.createOrderItem(orderItem);
            orderService.saveOrder(order);
        }
        return orderModels;
    }

    @GetMapping(value = "/order/customer/{id}")
    private ResponseEntity<Dictionary<String, Object>> retrieveCustomerOrder(@PathVariable("id") UUID id, HttpServletRequest httpServletRequest){
        // ApiScope.validateAuthority(httpServletRequest.getHeader(AUTHORIZATION), Constans.CUSTOMER_SELLER);

        List<OrderModel> listOrder = orderService.listByCustomer(id);
        Dictionary<String, Object> responseData = new Hashtable<>();
        responseData.put("status", HttpStatus.OK.value());
        responseData.put("message", "success");
        responseData.put("data", listOrder);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @GetMapping(value = "/order/seller/{id}")
    private ResponseEntity<Dictionary<String, Object>> retrieveSellerOrder(@PathVariable("id") UUID id, HttpServletRequest httpServletRequest) {
        List<Order> listOrderDto = orderService.listBySeller(id);

        Dictionary<String, Object> responseData = new Hashtable<>();
        responseData.put("status", HttpStatus.OK.value());
        responseData.put("message", "success");
        responseData.put("data", listOrderDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PutMapping("/order/update/{orderId}")
    public OrderModel updateOrderStatus(@PathVariable("orderId") UUID id,
            @Valid @RequestBody UpdateOrderRequestDTO orderDTO, BindingResult bindingResult) {
    
        System.out.println("Received request for orderId: " + id);
        OrderModel oldOrder = orderService.getOrderByOrderId(id);
    
        if(bindingResult.hasFieldErrors()){
            System.out.println("Invalid request body: " + bindingResult.getAllErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"); 
        } else {
            var order = orderMapper.updateOrderRequestDTOToOrder(orderDTO);
            order.setId(id);
            order.setStatus(orderDTO.getStatus());;
            OrderModel orderUpdated = orderService.updateOrder(order, oldOrder); 
    
            System.out.println("Order updated successfully: " + orderUpdated);
            return orderUpdated; 
        }
    }

    @GetMapping("/order/salesChart/{id}")
    public ResponseEntity<Map<LocalDate, Integer>> salesChart(@PathVariable("id") UUID id){
        Map<LocalDate, Integer> mapTotalOrdersPerDay = orderService.getDailySales(id);
        return ResponseEntity.ok(mapTotalOrdersPerDay);
    }

   
}
