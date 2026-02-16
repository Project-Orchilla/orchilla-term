package software.orchilla.ui.box.ui.displays;

import io.intino.alexandria.ui.Soul;
import software.orchilla.ui.box.ui.displays.templates.HomeTemplate;

public class RouteDispatcher extends AbstractRouteDispatcher {
	@Override
	public void dispatchHome(Soul soul) {
		soul.display(HomeTemplate.class).openHome();
	}

    @Override
    public void dispatchDoc(Soul soul) {
        soul.display(HomeTemplate.class).openDoc();
    }
}