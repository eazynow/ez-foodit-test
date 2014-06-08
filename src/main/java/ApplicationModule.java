import com.foodit.test.model.Menu;
import com.foodit.test.model.MenuItem;
import com.foodit.test.model.Order;
import com.foodit.test.model.OrderLineItem;
import com.foodit.test.model.RestaurantData;
import com.googlecode.objectify.ObjectifyService;
import com.threewks.thundr.gae.GaeModule;
import com.threewks.thundr.gae.objectify.ObjectifyModule;
import com.threewks.thundr.injection.BaseModule;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.module.DependencyRegistry;
import com.threewks.thundr.route.Routes;

public class ApplicationModule extends BaseModule {
	private ApplicationRoutes applicationRoutes = new ApplicationRoutes();

	@Override
	public void requires(DependencyRegistry dependencyRegistry) {
		super.requires(dependencyRegistry);
		dependencyRegistry.addDependency(GaeModule.class);
		dependencyRegistry.addDependency(ObjectifyModule.class);
	}

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		super.configure(injectionContext);
		configureObjectify();
	}

	@Override
	public void start(UpdatableInjectionContext injectionContext) {
		super.start(injectionContext);
		Routes routes = injectionContext.get(Routes.class);
		applicationRoutes.addRoutes(routes);
	}

	private void configureObjectify() {
		ObjectifyService.register(RestaurantData.class);
		ObjectifyService.register(Menu.class);
		ObjectifyService.register(MenuItem.class);
		ObjectifyService.register(Order.class);
		ObjectifyService.register(OrderLineItem.class);
	}
}
