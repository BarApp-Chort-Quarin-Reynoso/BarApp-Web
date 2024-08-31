package com.barapp.web.elements.views;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import in.virit.mopo.GridPw;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class ListaBaresViewPO {
    @Getter(AccessLevel.NONE)
    private final Page page;

    private final GridPw baresGrid;

    public ListaBaresViewPO(Page page) {
        this.page = page;
        baresGrid = new GridPw(page);
    }

    public void aceptarBar(String correo) {
        GridPw.RowPw row = getRowByCorreo(correo);
        row.getCell(5).getByLabel("Accept").click();
    }

    public void rechazarBar(String correo) {
        GridPw.RowPw row = getRowByCorreo(correo);
        row.getCell(5).getByLabel("Reject").click();
    }

    public Locator getEstadoBarLocator(String correo) {
        GridPw.RowPw row = getRowByCorreo(correo);
        return row.getCell(4).locator("span[theme~='badge']");
    }

    private GridPw.RowPw getRowByCorreo(String correo) {
        for (int i = 0; i < baresGrid.getRenderedRowCount(); i++) {
            GridPw.RowPw row = baresGrid.getRow(i);
            if (row.getCell(2).textContent().equals(correo)) {
                return row;
            }
        }

        return null;
    }
}
