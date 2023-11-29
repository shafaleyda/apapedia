package com.apapedia.order;

import java.time.LocalDateTime;
// import java.util.Locale;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.apapedia.order.model.OrderItemModel;
import com.apapedia.order.repository.OrderItemDb;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apapedia.order.dto.OrderMapper;
// import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.model.OrderModel;
import com.apapedia.order.service.OrderService;
// import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

	//CommandLineRunner untuk execute code saat spring pertama kali start up
	public static LocalDateTime generateRandomDateTime(LocalDateTime start, LocalDateTime end) {
		long startEpochSeconds = start.toEpochSecond(ZoneOffset.UTC);
		long endEpochSeconds = end.toEpochSecond(ZoneOffset.UTC);

		long randomEpochSeconds = ThreadLocalRandom.current().nextLong(startEpochSeconds, endEpochSeconds);

		return LocalDateTime.ofEpochSecond(randomEpochSeconds, 0, ZoneOffset.UTC);
	}

	@Bean
	@Transactional
	CommandLineRunner run(OrderService orderRestService, OrderMapper orderMapper, OrderItemDb orderItemDb){
		return args -> {
			for (int i = 0; i < 10; i++) {
				// var orderDTO = new CreateOrderRequestDTO();
				// orderDTO.setUpdatedAt(LocalDateTime.now());
				// orderDTO.setStatus(0); // Adjust the range as needed
				// System.out.println(orderDTO);
				OrderModel order = new OrderModel();
				// Save the fake order to the database
				// var order = orderMapper.createOrderRequestDTOToOrder(orderDTO);

				LocalDateTime startDateTime = LocalDateTime.of(2023, 11, 1, 0, 0); // Start date and time
				LocalDateTime endDateTime = LocalDateTime.of(2023, 11, 25, 23, 59); // End date and time
				LocalDateTime randomDateTime = generateRandomDateTime(startDateTime, endDateTime);


				order.setCreatedAt(randomDateTime);
				order.setUpdatedAt(LocalDateTime.now());
				order.setStatus(0);
				order.setTotalPrice(Integer.valueOf((int) (Math.random() * 1000000)));
				order.setCustomer(UUID.randomUUID());
				order.setSeller(UUID.randomUUID());
				orderRestService.saveOrder(order);
			}

			OrderItemModel orderItemModel = new OrderItemModel();
			orderItemModel.setProductId(UUID.fromString("e481094f-1df4-4198-8c32-77e4c3acf45e"));
			orderItemModel.setProductName("Aerodynamic Wooden Keyboard");
			orderItemModel.setProductPrice(37);
			orderItemModel.setOrder(orderRestService.getOrderByOrderId(UUID.fromString("cf54c65b-5ae2-4aea-9728-d97b66cea236")));
			orderItemModel.setQuantity(40);
			orderItemDb.save(orderItemModel);
		};
	}
}
