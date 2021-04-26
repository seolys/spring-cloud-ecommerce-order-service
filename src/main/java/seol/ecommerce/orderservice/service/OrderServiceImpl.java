package seol.ecommerce.orderservice.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import seol.ecommerce.orderservice.dto.OrderDto;
import seol.ecommerce.orderservice.exception.OrderNotFoundException;
import seol.ecommerce.orderservice.jpa.OrderEntity;
import seol.ecommerce.orderservice.jpa.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final ModelMapper mapper;
	private final OrderRepository orderRepository;

	@Override
	public OrderDto createOrder(OrderDto orderDto) {
		orderDto.setOrderId(UUID.randomUUID().toString());
		orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice()); // 수량 x 단가

		OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);

		OrderEntity savedOrderEntity = orderRepository.save(orderEntity);

		OrderDto savedOrderDto = mapper.map(savedOrderEntity, OrderDto.class);
		return savedOrderDto;
	}

	@Override
	public OrderDto getOrderByOrderId(String orderId) {
		Optional<OrderEntity> findOrder = orderRepository.findByOrderId(orderId);
		if (findOrder.isEmpty()) {
			throw new OrderNotFoundException();
		}
		
		OrderDto orderDto = mapper.map(findOrder.get(), OrderDto.class);
		return orderDto;
	}

	@Override
	public Iterable<OrderEntity> getOrdersByUserId(String userId) {
		return orderRepository.findByUserId(userId);
	}

}
