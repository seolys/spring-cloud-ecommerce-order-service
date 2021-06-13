package seol.ecommerce.orderservice.messagequeue.schema;

import java.util.List;
import seol.ecommerce.orderservice.dto.Field;
import seol.ecommerce.orderservice.dto.Schema;

public class OrderSchema {

	private static List<Field> orderFields = List.of(
			new Field("string", true, "order_id"),
			new Field("string", true, "user_id"),
			new Field("string", true, "product_id"),
			new Field("int32", true, "qty"),
			new Field("int32", true, "unit_price"),
			new Field("int32", true, "total_price")
	);

	private static Schema schema = Schema.builder()
			.type("struct")
			.fields(orderFields)
			.optional(false)
			.name("orders")
			.build();

	public static Schema getSchema() {
		return schema;
	}

}
