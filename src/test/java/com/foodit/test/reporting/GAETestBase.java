package com.foodit.test.reporting;

import static com.googlecode.objectify.ObjectifyService.ofy;

import org.junit.After;
import org.junit.Before;

import com.foodit.test.model.Menu;
import com.foodit.test.model.MenuItem;
import com.foodit.test.model.Order;
import com.foodit.test.model.OrderLineItem;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

public abstract class GAETestBase {

	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		 helper.setUp();
		 ObjectifyService.register(Menu.class);
		 ObjectifyService.register(MenuItem.class);
		 ObjectifyService.register(Order.class);
		 ObjectifyService.register(OrderLineItem.class);
	}
	
    @After
    public void tearDown() {
    	ofy().clear();
        helper.tearDown();
    }
}
