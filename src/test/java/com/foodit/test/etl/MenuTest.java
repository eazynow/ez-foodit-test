package com.foodit.test.etl;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

import com.foodit.test.GAETestBase;
import com.foodit.test.model.Menu;
import com.foodit.test.model.MenuItem;
import com.foodit.test.utils.FileHelper;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class MenuTest extends GAETestBase {

	@Test
	public void TestMenuWithNoItemsSavesCorrectly(){
		
		String menuId = "empty";
		String json = FileHelper.readFile("menu-empty.json");
		
		int count = ofy().load().type(Menu.class).count();
		assertEquals("There should be no menus to start", 0, count);
		
		Menu.saveMenuFromJson(json);
		
		count = ofy().load().type(Menu.class).count();
		assertEquals("There should be 1 menu now", 1, count);
		
		Menu menu = ofy().load().type(Menu.class).id(menuId).now();
		
		assertNotNull("The menu should be found by id", menu);
		
		int itemCount = ofy().load().type(MenuItem.class).count();
		assertEquals("There should be no menu items", 0, itemCount);
	}
	
	@Test
	public void TestMenuWithOneCategorySavesCorrectly(){

		String json = FileHelper.readFile("menu-onecat.json");
		
		// this matches the data in the json file
		String menuId = "onecat";
		
		int item1Id = 0;
		String item1Name = "Lamb Kebab";
		String item1Category = "Kebabs";
		
		int item2Id = 1;
		String item2Name = "Kofte Kebab";
		String item2Category = "More Kebabs";

		Menu.saveMenuFromJson(json);
		
		int count = ofy().load().type(Menu.class).count();		

		assertEquals("There should be 1 menu now", 1, count);
		
		Menu menu = ofy().load().type(Menu.class).id(menuId).now();
		
		assertNotNull("The menu should be found by id", menu);
		
		List<MenuItem> items = ofy().load().type(MenuItem.class).ancestor(menu).list();
		assertEquals("There should be 2 menu items", 2, items.size());
		
		boolean item1Found = false;
		boolean item2Found = false;

		for(MenuItem item : items){
			if(item.getMenuItemId() == item1Id) {
				assertEquals("Menu item 1 name should be correct", item1Name, item.getName());
				assertEquals("Menu item 1 category should be correct", item1Category, item.getCategory());
				item1Found = true;
			} else if (item.getMenuItemId() == item2Id) {
				assertEquals("Menu item 2 name should be correct", item2Name, item.getName());
				assertEquals("Menu item 2 category should be correct", item2Category, item.getCategory());
				item2Found = true;
			} else {
				fail("Unexpected menu item found in datastore");
			}
		}
		
		assertTrue("Menu item 1 should have been found", item1Found);
		assertTrue("Menu item 2 should have been found", item2Found);		
	}
}
