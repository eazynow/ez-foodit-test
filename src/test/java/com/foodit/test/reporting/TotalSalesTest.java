package com.foodit.test.reporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

import com.foodit.test.GAETestBase;
import com.foodit.test.TestDataHelper;
import com.foodit.test.model.Menu;
import com.foodit.test.model.Response.RestaurantStats;
import com.foodit.test.service.ReportingService;

public class TotalSalesTest extends GAETestBase{
	@Test
	public void testMultipleOrdersSalesTotalsCorrectly() {
		int ordersPerMenu = 10;
		BigDecimal totalPerOrder = new BigDecimal(5.5d).setScale(2, RoundingMode.HALF_UP);
		Menu menu = dataHelper.createMenu();
		dataHelper.createOrders(menu, totalPerOrder, ordersPerMenu);
		
		RestaurantStats stats = ReportingService.getTotalsForRestaurant(menu);

		BigDecimal totalSales = totalPerOrder.multiply(new BigDecimal(ordersPerMenu));
		
		assertEquals("Total totalSales should match", totalSales, stats.getTotalSales());
	}
	
	@Test
	public void testDifferentMenusHaveTheirCorrectSalesTotals() {
		int ordersPerMenu = 5;

		Menu menu1 = dataHelper.createMenu();
		BigDecimal menu1totalPerOrder = new BigDecimal(5.5d).setScale(2, RoundingMode.HALF_UP);
		dataHelper.createOrders(menu1, menu1totalPerOrder, ordersPerMenu);
		
		Menu menu2 = dataHelper.createMenu();
		BigDecimal menu2totalPerOrder = new BigDecimal(2.1d).setScale(2, RoundingMode.HALF_UP);
		dataHelper.createOrders(menu2, menu2totalPerOrder, ordersPerMenu);
		
		BigDecimal menu1sales = menu1totalPerOrder.multiply(new BigDecimal(ordersPerMenu));
		RestaurantStats menu1Stats = ReportingService.getTotalsForRestaurant(menu1);
		assertEquals("Menu1 totalSales should match", menu1sales, menu1Stats.getTotalSales());
		
		BigDecimal menu2sales = menu2totalPerOrder.multiply(new BigDecimal(ordersPerMenu));
		RestaurantStats menu2Stats = ReportingService.getTotalsForRestaurant(menu2);		
		assertEquals("Menu2 totalSales should match", menu2sales, menu2Stats.getTotalSales());
	}
	
	@Test
	public void testNoOrdersTotalsAsZero() {

		Menu menu = dataHelper.createMenu();
		
		BigDecimal emptyTotal = new BigDecimal(0d).setScale(2, RoundingMode.HALF_UP);
		RestaurantStats menuStats = ReportingService.getTotalsForRestaurant(menu);
		assertEquals("Menu totalSales should be zero", emptyTotal, menuStats.getTotalSales());
	}
}
