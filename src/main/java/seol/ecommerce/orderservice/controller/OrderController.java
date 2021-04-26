package seol.ecommerce.orderservice.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seol.ecommerce.orderservice.dto.OrderDto;
import seol.ecommerce.orderservice.jpa.OrderEntity;
import seol.ecommerce.orderservice.service.OrderService;
import seol.ecommerce.orderservice.vo.RequestOrder;
import seol.ecommerce.orderservice.vo.ResponseOrder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order-service")
public class OrderController {

	private final Environment environment;
	private final ModelMapper mapper;
	private final OrderService orderService;

	@GetMapping("/health_check")
	public String status() {
		return String.format("It's working in Order Service on PORT %s", environment.getProperty("local.server.port"));
	}

	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody @Valid RequestOrder order) {
		OrderDto orderDto = mapper.map(order, OrderDto.class);
		orderDto.setUserId(userId);

		OrderDto savedOrderDto = orderService.createOrder(orderDto);

		ResponseOrder responseOrder = mapper.map(savedOrderDto, ResponseOrder.class);

		return ResponseEntity
				.status(HttpStatus.CREATED) // NOTE: POST 요청에는 200(OK)이 아닌, 201(CREATED)를 반환하는것이 더 명확한 코드이다.
				.body(responseOrder);
	}

	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrder>> getUsers(@PathVariable("userId") String userId) {
		Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

		List<ResponseOrder> responseUsers = new ArrayList<>();
		orderList.forEach(v -> {
			responseUsers.add(mapper.map(v, ResponseOrder.class));
		});

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(responseUsers);
	}

}
