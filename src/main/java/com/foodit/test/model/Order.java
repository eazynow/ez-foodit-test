package com.foodit.test.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/**
 * Represents a single order for a restaurant
 * @author Erol Ziya
 */
@Entity
public class Order {
	
	@Id
	@SerializedName("orderId")
	long id;

	float totalValue;
	String status;
	
	// orderLineItems is only populated on deserializing of json data.
	// The datastore holds no reference 
	@SerializedName("lineItems")
	@Ignore
	List<OrderLineItem> orderLineItems;
	
	@Parent
	@Index
	transient Ref<Menu> menu;

	public Menu getMenu() {
		return menu.get();
	}
	public void setMenu(Menu menu) {
		this.menu = Ref.create(menu);
	}	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public BigDecimal getTotalValue() {
		return new BigDecimal(totalValue).setScale(2, RoundingMode.HALF_UP);
	}
	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue.floatValue();
	}
	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}
	public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Get a list of orders relating to a menu (restaurant)
	 * @param menu is the menu that the orders relate to
	 * @return an iterable list of orders
	 */
	public static List<Order> getOrdersForMenu(Menu menu) {
		// TODO: look at logic of the status field to see if they should be included
		return ofy().load().type(Order.class).ancestor(menu).list();
	}
	
	/**
	 * Parse and save orders relating to a menu to datastore
	 * @param menu is the id of the menu the orders relate to
	 * @param json is order data in json format
	 * @return any messages of what happened during saving
	 */
	public static List<String> saveOrdersFromJson(String menuId, String json){
		Gson gson = new GsonBuilder().create();
		List<Order> orders = Arrays.asList(gson.fromJson(json, Order[].class));
		List<String> messages = new ArrayList<String>();
		
		Menu menu = Menu.findById(menuId);
		
		if(menu == null) {
			// menu not found
			messages.add(String.format("Could not find menu [%s] in the datastore", menuId));
		} else {
			// load up the menu items list once
			Map<Integer, MenuItem> menuItems = MenuItem.getMenuItemsMapForMenu(menu);
	
			for(Order o: orders) {
				messages.addAll(saveOrder(menu, o, menuItems));
			}
			messages.add(String.format("Menu [%s] Imported %d orders", menuId, orders.size()));
		}

		return messages;
	}

	/**
	 * Save an order and it's related line items to datastore
	 * @param order to be saved, containing line items
	 * @param menuItems is a lookup of the itms for the menu
	 * @return any messages of what happened during saving
	 */
	private static List<String> saveOrder(Menu menu, Order order, Map<Integer, MenuItem> menuItems) {
		order.setMenu(menu);
		List<String> messages = new ArrayList<String>();
		
		// save the order with a reference to the menu
		ofy().save().entity(order).now();
		
		if(order.orderLineItems != null) {
			// set the parent document for each line item entry
			int itemId = 1;
			for(OrderLineItem li : order.orderLineItems){
				li.setOrder(order);
				li.setMenu(menu);
				li.setUniqueId(String.format("%s.%s.%d", menu.getId(), order.getId(), itemId));
				
				if(li.getMenuItemId() != null){
					//Delivery charges are items without id's
					if(!menuItems.containsKey(li.getMenuItemId())) {
						messages.add(
								String.format(
										"Missing menu item mapping. Menu [%s], order id [%s] line item id [%d]",
										menu.getId(), 
										order.getId(), 
										li.getMenuItemId()));
					} else {
						//Copy over the category and meal name as this data should be 
						//immutable for historical records, and it allows faster lookup for the API					
						MenuItem meal = menuItems.get(li.getMenuItemId());
						li.setCategory(meal.category);
						li.setMealName(meal.name);
					}
				}
				itemId++;
			}
			ofy().save().entities(order.orderLineItems);
		} else {
			messages.add(String.format("Warning. Order [%s] for menu [%s] has no line items", order.getId(), menu.getId()));
		}
		
		return messages;
	}
}
