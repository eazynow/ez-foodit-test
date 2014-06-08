package com.foodit.test.reporting;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.foodit.test.GAETestBase;
import com.foodit.test.TestDataHelper;
import com.foodit.test.model.Menu;
import com.foodit.test.model.Order;
import com.foodit.test.model.OrderLineItem;
import com.foodit.test.model.Response.RestaurantStats;
import com.foodit.test.service.ReportingService;

public class PopularCategoryTest extends GAETestBase {

	@Test
	public void testMostPopularCatIsCorrectWithOneOrderLine() {
		BigDecimal noCost = new BigDecimal(0);
		String popularCategory = "Kebabs";
		
		Menu menu = dataHelper.createMenu();
		Order o = dataHelper.createOrder(menu, noCost);
		dataHelper.createOrderLineItem(o, "", popularCategory, 1);
		
		RestaurantStats stats = ReportingService.getTotalsForRestaurant(menu);
		assertEquals("The most popular category should be Kebabs", popularCategory, stats.getMostPopularCategory().getName());
	}
	
	@Test
	public void testMostPopularCatIsCorrectWithMultipleOrderLinesOnOneOrder() {
		BigDecimal noCost = new BigDecimal(0);
		String popularCategory = "Kebabs";
		int popularCount = 10;
		String unpopularCategory = "Salad";
		int unpopularCount = 9;
		
		
		Menu menu = dataHelper.createMenu();
		Order o = dataHelper.createOrder(menu, noCost);
		
		dataHelper.createOrderLineItems(o, "", popularCategory, 1, popularCount);
		dataHelper.createOrderLineItems(o, "", unpopularCategory, 1, unpopularCount);
		
		List<OrderLineItem> items = OrderLineItem.getItemsForOrder(o);
		assertEquals("There should be 19 line items", popularCount + unpopularCount, items.size());
		
		RestaurantStats stats = ReportingService.getTotalsForRestaurant(menu);
		assertEquals("The most popular category should be Kebabs", popularCategory, stats.getMostPopularCategory().getName());
	}

	@Test
	public void testMostPopularCatIsCorrectWithMultipleOrderLinesOnMultipleOrders() {
		BigDecimal noCost = new BigDecimal(0);
		int orderCount = 5;
		String popularCategory = "Salad";
		int popularCount = 7;
		String unpopularCategory = "Kebabs";
		int unpopularCount = 6;

		Menu menu = dataHelper.createMenu();
		List<Order> orders = dataHelper.createOrders(menu, noCost, 5);
		for(Order o : orders) {
			dataHelper.createOrderLineItems(o, "", popularCategory, 1, popularCount);
			dataHelper.createOrderLineItems(o, "", unpopularCategory, 1, unpopularCount);			
		}

		int totalItems = ofy().load().type(OrderLineItem.class).count();
		assertEquals("There should be 65 line items", (popularCount + unpopularCount) * orderCount, totalItems);
		
		RestaurantStats stats = ReportingService.getTotalsForRestaurant(menu);
		assertEquals("The most popular category should be Salad", popularCategory, stats.getMostPopularCategory().getName());
	}
	
	@Test
	public void testMostPopularCatIsCorrectWithAHighQuantityOnOneLine() {
		BigDecimal noCost = new BigDecimal(0);
		String popularCategory = "Salad";
		int popularQuantity = 20;
		int popularCount = 1;
		
		String unpopularCategory = "Kebabs";
		int unpopularQuantity = 1;
		int unpopularCount = 19;

		Menu menu = dataHelper.createMenu();
		Order o = dataHelper.createOrder(menu, noCost);
		dataHelper.createOrderLineItems(o, "", popularCategory, popularQuantity, popularCount);
		dataHelper.createOrderLineItems(o, "", unpopularCategory, unpopularQuantity, unpopularCount);

		int totalItems = ofy().load().type(OrderLineItem.class).count();
		assertEquals("There should be 20 line items", popularCount + unpopularCount, totalItems);
		
		RestaurantStats stats = ReportingService.getTotalsForRestaurant(menu);
		assertEquals("The most popular category should be Salad", popularCategory, stats.getMostPopularCategory().getName());
	}
}
