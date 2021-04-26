package seol.ecommerce.orderservice.service;

import seol.ecommerce.orderservice.dto.OrderDto;
import seol.ecommerce.orderservice.jpa.OrderEntity;

public interface OrderService {

	OrderDto createOrder(OrderDto orderDetails);

	OrderDto getOrderByOrderId(String orderId);

	Iterable<OrderEntity> getOrdersByUserId(String userId);

}
