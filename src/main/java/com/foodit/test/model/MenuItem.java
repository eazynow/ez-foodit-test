package com.foodit.test.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/**
 * An individual menu item relating to a menu
 * @author Erol Ziya
 *
 */
@Entity
public class MenuItem {

	@Id
	String uniqueId;
	
	@Index
	@SerializedName("id")
	int menuItemid;

	String name;
	String category;
	
	@Parent
	Ref<Menu> menu;

	public Menu getMenu() {
		return menu.get();
	}
	public void setMenu(Menu menu) {
		this.menu = Ref.create(menu);
	}
	
	public String getUniqueId() {
		return uniqueId;
	}
	
	public void setUniqueId(String uniqueId){
		this.uniqueId = uniqueId;
	}
	
	public int getMenuItemId() {
		return menuItemid;
	}
	public void setMenuItemId(int id) {
		this.menuItemid = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * Parse and save menu items relating to a menu to datastore
	 * @param menu is the menu the items relate to
	 * @param json is menu items data in json format
	 */
	public static void saveMenuItemsFromJson(Menu menu, String itemsJson) {
		Gson gson = new GsonBuilder().create();
		List<MenuItem> items = Arrays.asList(gson.fromJson(itemsJson, MenuItem[].class));
		for(MenuItem item: items) {
			item.setUniqueId(String.format("%s.%s", menu.getId(), item.getMenuItemId()));
			item.setMenu(menu);
		}
		ofy().save().entities(items).now();
	}
	
	/**
	 * Lookup a menu item based on it's unique id
	 * @param menuItemId is the items id
	 * @return the menu item
	 */
	public static MenuItem findById(String menuItemId) {
		return ofy().load().type(MenuItem.class).id(menuItemId).now();
	}
	
	/**
	 * Get a map of menu items for a menu, keyed by id
	 * @param menu is the menu to use
	 * @return a map containing the items
	 */
	public static Map<Integer, MenuItem> getMenuItemsMapForMenu(Menu menu) {
		List<MenuItem> menuItemsList = ofy().load().type(MenuItem.class).ancestor(menu).list();
		
		return convertListToMap(menuItemsList);
	}
	
	/**
	 * Convert a list of menu items to a map keyed on unique id
	 * This makes lookup a lot quicker
	 * @param menuItemsList is the list of menu items to convert
	 * @return a map of the items keyed on unique id
	 */
	private static Map<Integer, MenuItem> convertListToMap(List<MenuItem> menuItemsList) {
		Map<Integer, MenuItem> menuItemsMap = new HashMap<Integer, MenuItem>();
		
		for (MenuItem item: menuItemsList) {
			menuItemsMap.put(item.getMenuItemId(), item);
		}
		return menuItemsMap;
	}
}
