package com.foodit.test.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/**
 * Individual Line Item in an order
 * @author Erol Ziya
 */

@Entity
public class OrderLineItem {

	@Id
	String uniqueId;

	@SerializedName("id")
	Integer menuItemId;

	String category;
	String mealName;
	
	int quantity;
	
	@Parent
	@Index
	Ref<Order> order;
	
	@Index
	Ref<Menu> menu;

	public String getUniqueId() {
		return uniqueId;
	}
	
	public void setUniqueId(String uniqueId){
		this.uniqueId = uniqueId;
	}
	
	public Integer getMenuItemId(){
		return menuItemId;
	}
	
	public void setMenuItemId(Integer menuItemId){
		this.menuItemId = menuItemId;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Order getOrder() {
		return order.get();
	}
	public void setOrder(Order order) {
		this.order = Ref.create(order);
	}
	
	public Menu getMenu() {
		return menu.get();
	}
	
	public void setMenu(Menu menu) {
		this.menu = Ref.create(menu);
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	/**
	 * Return all line items relating to an order
	 * @param parentOrder is the order to use
	 * @return a list of line items
	 */
	public static List<OrderLineItem> getItemsForOrder(Order parentOrder) {
		return ofy().load().type(OrderLineItem.class).ancestor(parentOrder).list();
	}
	
	/**
	 * Return all line items relating to a menu
	 * @param menu is the menu to use
	 * @return a list of line items
	 */
	public static List<OrderLineItem> getItemsForMenu(Menu menu) {
		return ofy().load().type(OrderLineItem.class).filter("menu", menu).list();
	}
	
	/**
	 * Return all line items in the datastore
	 * @return a list of all line items
	 */
	public static List<OrderLineItem> all() {
		return ofy().load().type(OrderLineItem.class).list();
	}
}
