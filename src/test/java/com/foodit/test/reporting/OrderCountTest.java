package com.foodit.test.reporting;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.foodit.test.GAETestBase;
import com.foodit.test.TestDataHelper;
import com.foodit.test.model.Menu;
import com.foodit.test.model.Response.RestaurantStats;
import com.foodit.test.service.ReportingService;

public class OrderCountTest extends GAETestBase {
	@Test
	public void testCorrectOrderCountWhenDataIsPresentForOneRestaurant() {
		int orderCount = 10;
		
		Menu m = dataHelper.createMenu();
		dataHelper.createOrders(m, new BigDecimal(0), orderCount);
		
		RestaurantStats stats = ReportingService.getTotalsForRestaurant(m);
		
		assertEquals("There should be 10 orders in the system for the menu", orderCount, stats.getTotalOrders());
	}

	@Test
	public void testCorrectOrderCountWhenDataIsPresentForAllRestaurants() {
		int menuCount = 4;
		int ordersPerMenu = 10;
		List<Menu> menus = dataHelper.createMenus(menuCount);
		
		for(Menu menu : menus){
			dataHelper.createOrders(menu, new BigDecimal(0), ordersPerMenu);
			RestaurantStats stats = ReportingService.getTotalsForRestaurant(menu);
		}
		
		List<RestaurantStats> allStats = ReportingService.getTotalsForRestaurants();
		for (RestaurantStats stats : allStats){
			assertEquals("There should be 10 orders in the system for each menu", ordersPerMenu, stats.getTotalOrders());
		}
	}

	@Test
	public void testCorrectOrderCountZeroWhenThereAreNone() {
		int orderCount = 0;
		
		Menu m = dataHelper.createMenu();
		
		RestaurantStats stats = ReportingService.getTotalsForRestaurant(m);
		
		assertEquals("There should be 0 orders in the system for the menu", orderCount, stats.getTotalOrders());
	}
}
