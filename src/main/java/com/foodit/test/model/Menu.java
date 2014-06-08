package com.foodit.test.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.threewks.thundr.logger.Logger;


/***
 * Defines a menu for a restaurant
 * @author Erol Ziya
 */
@Entity
public class Menu {
	// for now lets make this the restaurant menuItemid as it's 1-to-1 for the test
	// In reality restaurants may have multiple menus
	@Id
	@SerializedName("restaurantId")
	String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Parse and save a menu to datastore
	 * @param id is the id of the restaurant the menu relates to
	 * @param json is order data in json format
	 * @return any messages of what happened during saving
	 */
	public static List<String> saveMenuFromJson(String json) {
		List<String> messages = new ArrayList<String>();	
		String id = null;
		try {
			JSONObject joMenu = new JSONObject(json);
			
			id = joMenu.getString("restaurantId");
			Menu menu = new Menu();
			menu.setId(id);
			
			ofy().save().entity(menu).now();
			int catCount = processMenuCategories(joMenu, menu);
			
			messages.add(String.format("Menu [%s] Imported %d categories", id, catCount));
			
		} catch (JSONException e) {
			messages.add(String.format("Menu [%s] Could not decode json: %s", id, e.getMessage()));
		}
		
		return messages;
	}

	/**
	 * Parse the individual menu categories and convert to menu items
	 * @param joMenu the json object holding the categories
	 * @param menu is the menu they will belong to
	 * @return the total categories processed
	 * @throws JSONException thrown if the json could not be parsed
	 */
	private static int processMenuCategories(JSONObject joMenu, Menu menu) throws JSONException {
		JSONObject joMenuCats = joMenu.getJSONObject("menu");
		Iterator<String> iCats = joMenuCats.keys();
		
		Logger.info("Processing Menu categories:");
		int catCount = 0;
		while(iCats.hasNext()) {
			String itemsJson =  joMenuCats.getString(iCats.next());
			
			MenuItem.saveMenuItemsFromJson(menu, itemsJson);
			catCount++;
		}
		return catCount;
	}

	public static Menu findById(String storeId) {
		return ofy().load().type(Menu.class).id(storeId).now();
	}

	public static Iterable<Menu> all() {
		return ofy().load().type(Menu.class).iterable();
	}
}
