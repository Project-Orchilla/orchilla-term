package software.orchilla.ui.box.ui.pages;

import io.intino.alexandria.exceptions.*;
import java.time.*;
import java.util.*;
import software.orchilla.ui.box.ui.displays.templates.*;

public class HomePage extends AbstractHomePage {
	public String dialog;

	public io.intino.alexandria.ui.Soul prepareSoul(io.intino.alexandria.ui.services.push.UIClient client) {
		return new io.intino.alexandria.ui.Soul(session) {
			@Override
			public void personify() {
				HomeTemplate component = new HomeTemplate(box);
				register(component);
				component.init();
			}
		};
	}
}