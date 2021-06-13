package seol.ecommerce.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import seol.ecommerce.orderservice.dto.KafkaOrderDto;
import seol.ecommerce.orderservice.dto.OrderDto;
import seol.ecommerce.orderservice.dto.OrderPayload;
import seol.ecommerce.orderservice.messagequeue.schema.OrderSchema;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;


	public OrderDto send(String topic, OrderDto orderDto) {
		OrderPayload payload = OrderPayload.builder()
				.order_id(orderDto.getOrderId())
				.user_id(orderDto.getUserId())
				.product_id(orderDto.getProductId())
				.qty(orderDto.getQty())
				.unit_price(orderDto.getUnitPrice())
				.total_price(orderDto.getTotalPrice())
				.build();
		KafkaOrderDto kafkaOrderDto = KafkaOrderDto.builder()
				.schema(OrderSchema.getSchema())
				.payload(payload)
				.build();

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(kafkaOrderDto);
		} catch (JsonProcessingException e) {
			log.error("writeValueAsString Error", e);
		}
		kafkaTemplate.send(topic, jsonInString);
		log.info("Kafka Producer sent data from the Order microservice: {}", kafkaOrderDto);

		return orderDto;
	}

}
