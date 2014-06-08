package com.foodit.test.model.Response;

import java.util.List;

public class OverallStats {
	List<CountStatistic> mostPopularMeals;
	
	public List<CountStatistic> getMostPopularMeals() {
		return mostPopularMeals;
	}

	public void setMostPopularMeals(List<CountStatistic> mostPopularMeals) {
		this.mostPopularMeals = mostPopularMeals;
	}	
}
