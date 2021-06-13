package seol.ecommerce.orderservice.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class KafkaOrderDto implements Serializable {

	private Schema schema;

	private OrderPayload payload;

}
