package software.orchilla.ui.box.ui.displays.templates;

import io.intino.alexandria.ui.displays.components.BlockConditional;
import software.orchilla.ui.box.UiBox;

import java.util.Arrays;

public class HomeTemplate extends AbstractHomeTemplate<UiBox> {
    private Page current;

    public HomeTemplate(UiBox box) {
		super(box);
        this.current = null;
	}

    public void openLanding() {
        openPage(Page.Landing);
        if (landingPage.landingStamp != null) {
            landingPage.landingStamp.refresh();
        }
    }

    public void openHome() {
        openLanding();
    }

    public void openDoc() {
        openPage(Page.Doc);
        if (docPage.docStamp != null) {
            docPage.docStamp.refresh();
        }
    }

    private boolean openPage(Page page) {
        if (current == page) return true;
        hidePages();
        blockOf(page).show();
        current = page;
        return true;
    }

    private BlockConditional<?, ?> blockOf(Page page) {
        if (page == Page.Landing) return landingPage;
        if (page == Page.Doc) return docPage;
        return null;
    }

    private void hidePages() {
        if (landingPage.isVisible()) landingPage.hide();
        if (docPage.isVisible()) docPage.hide();
    }

    public enum Page {
        Landing, Doc;

        public static Page from(String key) {
            return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(key)).findFirst().orElse(null);
        }
    }

}