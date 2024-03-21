package com.barapp.web.views.utils.components;

import com.flowingcode.vaadin.addons.errorwindow.ErrorWindow;
import com.flowingcode.vaadin.addons.errorwindow.ErrorWindowI18n;
import com.vaadin.flow.component.UI;

public class CustomErrorWindow {

    public static void showError(final Throwable cause) {
        ErrorWindowI18n i18n = ErrorWindowI18n.createDefault();
        i18n.setCaption(UI.getCurrent().getTranslation("flowingcode.addons.errorwindow.caption"));
        i18n.setInstructions(UI.getCurrent().getTranslation("flowingcode.addons.errorwindow.instruction"));
        i18n.setClose(UI.getCurrent().getTranslation("flowingcode.addons.errorwindow.close"));
        i18n.setDetails(UI.getCurrent().getTranslation("flowingcode.addons.errorwindow.details"));
        i18n.setDefaultErrorMessage(UI.getCurrent().getTranslation("flowingcode.addons.errorwindow.defaultErrorMessage"));
        i18n.setClipboard(UI.getCurrent().getTranslation("flowingcode.addons.errorwindow.clipboard"));

        ErrorWindow w = new ErrorWindow(cause, i18n);
        w.open();
    }
}
