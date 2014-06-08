import com.foodit.test.controller.DataLoadController;
import com.foodit.test.controller.ETLController;
import com.foodit.test.controller.OrderController;
import com.foodit.test.controller.RestaurantController;
import com.foodit.test.controller.SummaryController;
import com.threewks.thundr.action.method.MethodAction;
import com.threewks.thundr.route.Route;
import com.threewks.thundr.route.Routes;

import static com.threewks.thundr.route.RouteType.GET;

public class ApplicationRoutes {
	public static class Names {
		public static final String ListTasks = "list";
		public static final String CreateTask = "create-task";
		public static final String ViewTask = "view-task";
		public static final String UpdateTask = "update-task";
		public static final String StartTask = "start-task";
		public static final String StopTask = "stop-task";
		public static final String FinishedTask = "finished-task";
		public static final String ArchiveTask = "archive-task";
		public static final String LoadData = "load-data";
		public static final String ViewInstructions = "view-instructions";
		public static final String ViewData = "view-data";
		
		public static final String PerformETL = "etl-data";
		public static final String ViewSummaryAPI = "api-summary";
		public static final String ViewRestaurants = "api-restaurants";
		public static final String ViewRestaurant = "api-restaurant";
		public static final String ViewRestaurantOrders = "api-restaurant-orders";
	}

	public void addRoutes(Routes routes) {

		// Loader
		routes.addRoute(new Route(GET, "/load/", Names.LoadData), new MethodAction(DataLoadController.class, "load"));

		// Instructions
		routes.addRoute(new Route(GET, "/", Names.ViewInstructions), new MethodAction(DataLoadController.class, "instructions"));

		// Perform ETL. Transform the sample data to datastore entities
		// In pure REST this should be a POST as it modifies data
		// but a get is easier to illustrate and call as a GET in the test		
		routes.addRoute(new Route(GET, "/etl", Names.PerformETL), new MethodAction(ETLController.class, "performETL"));
		
		// Restaurant API's
		routes.addRoute(new Route(GET, "/restaurants", Names.ViewRestaurants), new MethodAction(RestaurantController.class, "getTotalsForRestaurants"));
		routes.addRoute(new Route(GET, "/restaurants/{restaurant}", Names.ViewRestaurant), new MethodAction(RestaurantController.class, "getTotalsForRestaurant"));
		routes.addRoute(new Route(GET, "/restaurants/{restaurant}/download", Names.ViewData), new MethodAction(DataLoadController.class, "viewData"));
		routes.addRoute(new Route(GET, "/restaurants/{restaurant}/orders", Names.ViewRestaurantOrders), new MethodAction(OrderController.class, "getOrdersForRestaurant"));
		
		
		// Summary API's
		routes.addRoute(new Route(GET, "/summary", Names.ViewSummaryAPI), new MethodAction(SummaryController.class, "getTotalsForSummary"));
	}
}
