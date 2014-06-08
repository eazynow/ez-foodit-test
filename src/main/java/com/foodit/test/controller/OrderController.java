package com.foodit.test.controller;

import com.foodit.test.model.Menu;
import com.foodit.test.model.Order;
import com.threewks.thundr.view.json.JsonView;

public class OrderController {

	public JsonView getOrdersForRestaurant(String restaurant) {
		Menu menu = Menu.findById(restaurant);
		return new JsonView(Order.getOrdersForMenu(menu));
	}
}
