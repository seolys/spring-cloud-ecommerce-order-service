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
import seol.ecommerce.orderservice.messagequeue.KafkaProducer;
import seol.ecommerce.orderservice.messagequeue.OrderProducer;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final ModelMapper mapper;
	private final OrderRepository orderRepository;
	private final KafkaProducer kafkaProducer;
	private final OrderProducer orderProducer;

	@Override
	public OrderDto createOrder(OrderDto orderDto) {
		orderDto.setOrderId(UUID.randomUUID().toString());
		orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice()); // 수량 x 단가

		// JPA
		OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);
		OrderEntity savedOrderEntity = orderRepository.save(orderEntity); // JPA통한 DB저장이 아닌, Kafka를 거쳐서 DB에 저장되도록 한다.
//		OrderDto savedOrderDto = mapper.map(savedOrderEntity, OrderDto.class);

		// Kafka
		kafkaProducer.send("example-catalog-topic", orderDto);
//		orderProducer.send("orders", orderDto); // DB 저장

		return orderDto;
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
