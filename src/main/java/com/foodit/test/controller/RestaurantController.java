package com.foodit.test.controller;

import javax.servlet.http.HttpServletResponse;

import com.foodit.test.model.Menu;
import com.foodit.test.service.ReportingService;
import com.threewks.thundr.http.exception.HttpStatusException;
import com.threewks.thundr.view.json.JsonView;

public class RestaurantController {
	
	public JsonView getTotalsForRestaurants(){

		return new JsonView(ReportingService.getTotalsForRestaurants());
	}
	
	public JsonView getTotalsForRestaurant(String restaurant){
		Menu menu = Menu.findById(restaurant);
		if(menu != null){ 
			return new JsonView(ReportingService.getTotalsForRestaurant(menu));
		} else {
			throw new HttpStatusException(HttpServletResponse.SC_NOT_FOUND, "Could not find restaurant %s", restaurant);
		}
	}
}
