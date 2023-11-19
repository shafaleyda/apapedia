package com.apapedia.order;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apapedia.order.dto.OrderMapper;
import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.model.OrderModel;
import com.apapedia.order.restservice.OrderRestService;
import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
	
	//CommandLineRunner untuk execute code saat spring pertama kali start up

	@Bean
	@Transactional
	CommandLineRunner run(OrderRestService orderRestService, OrderMapper orderMapper){
		return args -> {
			for (int i = 0; i < 10; i++) {
				// var orderDTO = new CreateOrderRequestDTO();		
				// orderDTO.setUpdatedAt(LocalDateTime.now());		
				// orderDTO.setStatus(0); // Adjust the range as needed
				// System.out.println(orderDTO);
				OrderModel order = new OrderModel();
				// Save the fake order to the database
				// var order = orderMapper.createOrderRequestDTOToOrder(orderDTO);
				order.setCreatedAt(LocalDateTime.now());
				order.setUpdatedAt(LocalDateTime.now());		
				order.setStatus(0); 
				order.setTotalPrice((long) (int) (Math.random() * 1000000));
				order.setCustomer(UUID.randomUUID());
				order.setSeller(UUID.randomUUID());
				orderRestService.saveOrder(order);
				
        	}
		};
	}
}
