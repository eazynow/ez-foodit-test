package com.foodit.test.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.foodit.test.model.Menu;
import com.foodit.test.model.Order;
import com.foodit.test.model.OrderLineItem;
import com.foodit.test.model.Response.CountStatistic;
import com.foodit.test.model.Response.OverallStats;
import com.foodit.test.model.Response.RestaurantStats;
import com.foodit.test.utils.MapHelper;
import com.threewks.thundr.logger.Logger;

/**
 * ReportingService holds all the logic relating to the reporting
 * elements of the test. Keeping the reporting logic separate from
 * the controller allows reuse beyond just thundr and makes it easier
 * to unit test the logic directly
 * @author Erol Ziya
 */
public class ReportingService  {
	
	/**
	 * Get reporting statistics for all restaurants.
	 * @return a list of all restaurants with their stats
	 */
	public static List<RestaurantStats> getTotalsForRestaurants(){

		List<RestaurantStats> restaurants = new ArrayList<RestaurantStats>();
		
		for (Menu menu: Menu.all()) {
			restaurants.add(getTotalsForRestaurant(menu));
		}
		
		return restaurants;
	}

	/**
	 * Get reporting statistics for an individual restaurant.
	 * @return The stats for the restaurant
	 */
	public static RestaurantStats getTotalsForRestaurant(Menu menu) {		
		List<Order> orders = Order.getOrdersForMenu(menu);

		RestaurantStats stats = new RestaurantStats();
		
		stats.setRestaurant(menu.getId());
		stats.setTotalOrders(orders.size());
		stats.setTotalSales(getTotalSalesForOrders(orders));
		stats.setMostPopularCategory(getFaveCatsForMenu(menu));

		return stats;
	}
	
	/**
	 * Calculate most popular mostPopularCategory for a list of orders
	 * @param menu
	 * @return
	 */
	private static CountStatistic getFaveCatsForMenu(Menu menu) {
		List<OrderLineItem> items = OrderLineItem.getItemsForMenu(menu);
		CountStatistic mostPopular = new CountStatistic();
		
		Map<String, Integer> catCount = new HashMap<String, Integer>();
		
		for(OrderLineItem li : items){
			String cat = li.getCategory();
			if(cat != null) {
				// only include if the category was populated
				// it won't be if the ETL couldn't map it previously
				Integer currentCount = catCount.get(cat);

				// increment count for that category. Use the quantity
				// for more realistic weighting
				currentCount = (currentCount == null)?li.getQuantity():currentCount+li.getQuantity();

				if (currentCount > mostPopular.getCount()) {
					// there is a new most popular category!
					mostPopular.setCategory(cat);
					mostPopular.setCount(currentCount);
				}
				
				// update the map with new total
				catCount.put(cat, currentCount);
			}
		}
		
		return mostPopular;
	}

	/**
	 * Calculate total totalSales 
	 * @param orders are the orders to total up
	 * @return the total sales
	 */
	private static BigDecimal getTotalSalesForOrders(List<Order> orders) {
		BigDecimal totalSales = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

		for (Order order: orders) {	
			// add the total totalSales
			totalSales = totalSales.add(order.getTotalValue());
		}
				
		return totalSales;
	}

	/**
	 * Calculate overview statistics. Currently the most popular meals
	 * @param maxItems is maximum number of items to return. If null it defaults to 10
	 * @return the most popular meals
	 */
	public static OverallStats getTotalsForSummary(Integer maxItems) {
		
		Map<String, Integer> mostPopular = getMostPopularMealFromLineItems(OrderLineItem.all());
		
		maxItems = (maxItems==null)?10:maxItems;
		
		OverallStats stats = new OverallStats();

		// the map helper will shift the items into sorted order
		// and apply the maximum limit
		stats.setMostPopularMeals(MapHelper.sortMapIntoOrderedList(mostPopular, false, maxItems));

		return stats;
	}

	/**
	 * loop through order line items provided and total up most popular meal 
	 * @param items
	 * @return a map listing the items and their counts
	 */
	private static Map<String, Integer> getMostPopularMealFromLineItems(List<OrderLineItem> items) {
		Map<String, Integer> mostPopular = new HashMap<String, Integer>();
		for (OrderLineItem li : items) {
			String meal = li.getMealName();
			if(meal != null) {
				Integer currentCount = mostPopular.get(meal);

				// increment count - use quantity for more realistic weighting
				currentCount = (currentCount == null)?li.getQuantity():currentCount+li.getQuantity();
				
				mostPopular.put(meal, currentCount);
			}
		}
		
		return mostPopular;
	}
}
