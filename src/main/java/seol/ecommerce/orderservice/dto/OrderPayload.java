package seol.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OrderPayload {

	private String order_id;

	private String user_id;

	private String product_id;

	private int qty;

	private int unit_price;

	private int total_price;

}
