package software.orchilla.ui.box.ui.displays.templates;

import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.logger.Logger;
import software.orchilla.ui.box.*;
import software.orchilla.ui.box.schemas.*;
import software.orchilla.ui.box.UiBox;
import software.orchilla.ui.box.ui.displays.templates.AbstractDocTemplate;

import java.io.IOException;

public class DocTemplate extends AbstractDocTemplate<UiBox> {

	public DocTemplate(UiBox box) {
		super(box);
	}

    @Override
    public void refresh() {
        super.refresh();
        viewer.content(content());
        viewer.refresh();
    }

    public String content() {
        try {
            return new String(getClass().getResource("/docs/doc.html").openStream().readAllBytes());
        } catch (IOException e) {
            Logger.error(e);
        }
        return "";
    }
}
