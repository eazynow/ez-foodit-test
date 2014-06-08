package com.foodit.test;

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

/**
 * Provides an abstract superclass to setup GAE datastore for tests
 * @author Erol Ziya
 *
 */
public abstract class GAETestBase {

	protected final TestDataHelper dataHelper = new TestDataHelper();

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
