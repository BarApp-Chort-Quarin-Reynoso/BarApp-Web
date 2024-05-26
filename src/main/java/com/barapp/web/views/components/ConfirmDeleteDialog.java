package com.barapp.web.views.components;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

public class ConfirmDeleteDialog extends ConfirmDialog {
    public ConfirmDeleteDialog(String title, String text) {
        setCancelable(true);
        setCancelButton(getTranslation("commons.cancel"), event -> {});
        setConfirmButton(getTranslation("commons.eliminar"), event -> {});
        setHeader(title);
        setText(text);
    }
}
