package com.barapp.web.utils;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class FormatUtils {
    public static DatePicker.DatePickerI18n datePickerI18n() {
        DatePicker.DatePickerI18n spanishI18n = new DatePicker.DatePickerI18n();
        spanishI18n.setMonthNames(List.of("Enero", "Febrero", "Marzo", "Abril",
                "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre",
                "Noviembre", "Diciembre"
        ));
        spanishI18n.setWeekdays(List.of("Domingo", "Lunes", "Martes",
                "Miércoles", "Jueves", "Viernes", "Sábado"
        ));
        spanishI18n.setWeekdaysShort(
                List.of("Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa"));
        spanishI18n.setToday("Hoy");
        spanishI18n.setCancel("Cancelar");

        return spanishI18n;
    }

    public static DateTimeFormatter timeFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    public static DateTimeFormatter timestampFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    public static DateTimeFormatter visualizationDateFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public static DateTimeFormatter persistenceDateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public static NumberFormat puntuacionFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        return new DecimalFormat("0.0", symbols);
    }
}
