package com.foodit.test.etl;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

import com.foodit.test.GAETestBase;
import com.foodit.test.model.Menu;
import com.foodit.test.model.Order;
import com.foodit.test.model.OrderLineItem;
import com.foodit.test.utils.FileHelper;

public class OrderTest extends GAETestBase {

	@Test
	public void TestOrderWithNoItemsSavesCorrectly(){
		
		long orderId = 5456722831343616l;
		BigDecimal total = new BigDecimal(0).setScale(2,RoundingMode.HALF_UP);
		
		String menuId = "empty";
		Menu menu = new Menu();
		menu.setId(menuId);
		ofy().save().entities(menu).now();

		String json = FileHelper.readFile("order-empty.json");

		int count = ofy().load().type(Order.class).ancestor(menu).count();
		assertEquals("There should be no orders to start", 0, count);
		
		Order.saveOrdersFromJson(menuId, json);
		
		count = ofy().load().type(Order.class).count();
		assertEquals("There should be 1 order now", 1, count);
		
		count = ofy().load().type(OrderLineItem.class).count();
		assertEquals("There should be 0 order line items", 0, count);
		
		Order o = ofy().load().type(Order.class).first().now();
		assertEquals("Order id should match", orderId, o.getId());
		assertEquals("Order value should match", total, o.getTotalValue());

	}
}