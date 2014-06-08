package com.foodit.test.model.Response;

import java.math.BigDecimal;

public class RestaurantStats {

	String restaurant;
	int totalOrders;
	BigDecimal totalSales;
	CountStatistic mostPopularCategory;

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	public int getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}

	public BigDecimal getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(BigDecimal sales) {
		this.totalSales = sales;
	}

	public CountStatistic getMostPopularCategory() {
		return mostPopularCategory;
	}

	public void setMostPopularCategory(CountStatistic mostPopularCategory) {
		this.mostPopularCategory = mostPopularCategory;
	}	
}
