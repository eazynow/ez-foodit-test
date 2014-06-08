package com.foodit.test.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.foodit.test.controller.DataLoadController;
import com.foodit.test.model.Menu;
import com.foodit.test.model.Order;
import com.foodit.test.model.RestaurantData;

public class ETLService {

	/**
	 * Converts the base test data into separate documents in GAE.
	 * Also checks for any broken data links and reports them.
	 * @return a list of messages relating to the etl process
	 */
	public static List<String> performETL() {
		// first ensure the initial data is loaded into datastore
		DataLoadController dlc = new DataLoadController();
		dlc.load();
		
		List<String> messages = new ArrayList<String>();
		
		List<RestaurantData> restaurants = ofy().load().type(RestaurantData.class).list(); 
		messages.add(String.format("Importing %d restaurants", restaurants.size()));
		
		for(RestaurantData rd : restaurants ){
			messages.addAll(Menu.saveMenuFromJson(rd.getMenuJson().getValue()));
			messages.addAll(Order.saveOrdersFromJson(rd.getRestaurant(), rd.getOrdersJson().getValue()));
		}
		
		return messages;
			
	}

}
