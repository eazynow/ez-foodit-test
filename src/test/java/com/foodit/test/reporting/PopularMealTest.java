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
import com.foodit.test.model.Response.CountStatistic;
import com.foodit.test.model.Response.OverallStats;
import com.foodit.test.service.ReportingService;

public class PopularMealTest extends GAETestBase {	

	@Test
	public void testMostPopularMealWorksForOneRestaurant() {
		BigDecimal noCost = new BigDecimal(0);
		String popularMeal = "Doner Kebab";
		String popularCategory = "Kebabs";
		int popularQuantity = 1;
		int popularCount = 20;
		
		String unpopularMeal = "Caesar Salad";
		String unpopularCategory = "Salad";
		int unpopularQuantity = 1;
		int unpopularCount = 19;

		Menu menu = dataHelper.createMenu();
		Order o = dataHelper.createOrder(menu, noCost);
		dataHelper.createOrderLineItems(o, popularMeal, popularCategory, popularQuantity, popularCount);
		dataHelper.createOrderLineItems(o, unpopularMeal, unpopularCategory, unpopularQuantity, unpopularCount);

		int totalItems = ofy().load().type(OrderLineItem.class).count();
		assertEquals("There should be 20 line items", popularCount + unpopularCount, totalItems);
		
		OverallStats stats = ReportingService.getTotalsForSummary(2);
		List<CountStatistic> totals = stats.getMostPopularMeals();

		// grab the first (top) entry
		CountStatistic mostPopular = totals.get(0);		
		assertEquals("Doner Kebab should the most popular", popularMeal, mostPopular.getName());
		assertEquals("Doner Kebab should have 20 as a count", popularCount * popularQuantity, mostPopular.getCount());
	}
	
	@Test
	public void testMostPopularMealOrdersCorrectlyWithAHighQuantity() {
		BigDecimal noCost = new BigDecimal(0);
		String popularMeal = "Doner Kebab";
		String popularCategory = "Kebabs";
		int popularQuantity = 6;
		int popularCount = 2;
		
		String unpopularMeal = "Caesar Salad";
		String unpopularCategory = "Salad";
		int unpopularQuantity = 1;
		int unpopularCount = 11;

		Menu menu = dataHelper.createMenu();
		Order o = dataHelper.createOrder(menu, noCost);
		dataHelper.createOrderLineItems(o, popularMeal, popularCategory, popularQuantity, popularCount);
		dataHelper.createOrderLineItems(o, unpopularMeal, unpopularCategory, unpopularQuantity, unpopularCount);

		int totalItems = ofy().load().type(OrderLineItem.class).count();
		assertEquals("There should be 13 line items", popularCount + unpopularCount, totalItems);
		
		OverallStats stats = ReportingService.getTotalsForSummary(2);
		List<CountStatistic> totals = stats.getMostPopularMeals();

		// grab the first (top) entry
		CountStatistic mostPopular = totals.get(0);	

		assertEquals("Doner Kebab should the most popular", popularMeal, mostPopular.getName());

		assertEquals("Doner Kebab should have 12 as a count", popularCount * popularQuantity, mostPopular.getCount());
	}

}
