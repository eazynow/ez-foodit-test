package com.foodit.test;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.foodit.test.model.Menu;
import com.foodit.test.model.Order;
import com.foodit.test.model.OrderLineItem;
/**
 * Helper class for quickly creating datastore data for tests
 * @author Erol Ziya
 *
 */
public class TestDataHelper {
	private long uniqueId;
	
	/**
	 * Get a unique long (for order id's mainly)
	 * @return a unique long
	 */
	private synchronized long nextLong() {
		this.uniqueId ++;
		return this.uniqueId;
	}

	/**
	 * Create a menu with a random id, just in memory
	 * @return the new menu
	 */
	private Menu setupMenu(){
		Menu m = new Menu();
		m.setId(UUID.randomUUID().toString());
		return m;
	}

	/**
	 * Create a menu with a random id, and save it to datastore
	 * @return the new menu
	 */
	public Menu createMenu(){
		Menu m = setupMenu();
		ofy().save().entities(m).now();
		return m;
	}
	
	/**
	 * Create a number of menus and save them to the datastore
	 * @param count the number of menus to create
	 * @return a list of the new menus
	 */
	public List<Menu> createMenus(int count){
		List<Menu> menus = new ArrayList<Menu>();
		for(int i = 1;i<=count;i++){
			menus.add(setupMenu());
		}

		ofy().save().entities(menus).now();
		return menus;	
	}
	
	/**
	 * Create a new order for a menu with a preset total value
	 * @param menu is the menu the order relates to
	 * @param value is the total sales value
	 * @return the new order
	 */
	private Order setupOrder(Menu menu, BigDecimal value){
		Order o = new Order();
		o.setId(nextLong());
		o.setMenu(menu);
		o.setTotalValue(value);	
		return o;
	}
	
	/**
	 * Create a new order for a menu with a preset total value
	 * and save to the datastore
	 * @param menu is the menu the order relates to
	 * @param value is the total sales value
	 * @return the new order
	 */
	public Order createOrder(Menu menu, BigDecimal value){
		Order o = setupOrder(menu, value);
		ofy().save().entities(o).now();
		return o;
	}
	
	/**
	 * Create multiple new orders for a menu with a preset total value
	 * and save to the datastore
	 * @param menu is the menu the order relates to
	 * @param value is the total sales value
	 * @param count is how many orders to create
	 * @return the list of new orders
	 */
	public List<Order> createOrders(Menu menu, BigDecimal value, int count){
		List<Order> orders = new ArrayList<Order>();
		for(int i = 1;i<=count;i++){
			// need random id's to stop clashes
			orders.add(setupOrder(menu, value));
		}
		ofy().save().entities(orders).now();

		return orders;
	}
	
	/**
	 * Create a new line item for an order with the set meal name, 
	 * category and quantity
	 * @param order is the order the line item belongs to
	 * @param mealName is the name of the meal
	 * @param category is the category the item belongs to
	 * @param quantity is how many were ordered
	 * @return the new line item
	 */
	private OrderLineItem setupOrderLineItem(Order order, String mealName, String category, int quantity) {
		OrderLineItem li = new OrderLineItem();
		li.setOrder(order);
		li.setMenu(order.getMenu());
		li.setMealName(mealName);
		li.setCategory(category);
		li.setQuantity(quantity);
		li.setUniqueId(UUID.randomUUID().toString());
		return li;
	}
	
	/**
	 * Create a new line item for an order with the set meal name, 
	 * category and quantity and save to the datastore
	 * @param order is the order the line item belongs to
	 * @param mealName is the name of the meal
	 * @param category is the category the item belongs to
	 * @param quantity is how many were ordered
	 * @return the new line item
	 */
	public OrderLineItem createOrderLineItem(Order order, String mealName, String category, int quantity) {
		OrderLineItem li = setupOrderLineItem(order, mealName, category, quantity);
		ofy().save().entities(li).now();
		return li;
	}
	
	/**
	 * Create multiple new line items for an order with the set 
	 * meal name, category and quantity
	 * @param order is the order the line item belongs to
	 * @param mealName is the name of the meal
	 * @param category is the category the item belongs to
	 * @param quantity is how many were ordered
	 * @return the list of new line items
	 */
	public List<OrderLineItem> createOrderLineItems(Order order, String mealName, String category, int quantity, int count){
		List<OrderLineItem> items = new ArrayList<OrderLineItem>();
		for(int i = 1;i<=count;i++){
			// need random id's to stop clashes
			items.add(setupOrderLineItem(order, mealName, category, quantity));
		}
		ofy().save().entities(items).now();

		return items;
	}

}
