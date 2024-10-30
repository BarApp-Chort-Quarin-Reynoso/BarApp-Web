package com.barapp.web.utils;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class FormatUtils {
    public static List<String> getWeekdays() {
        return List.of("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado");
    }

    public static List<String> getWeekdaysShort() {
        return List.of("Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa");
    }

    public static List<String> getMonths() {
        return List.of("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
    }

    public static DatePicker.DatePickerI18n datePickerI18n() {
        DatePicker.DatePickerI18n spanishI18n = new DatePicker.DatePickerI18n();
        spanishI18n.setMonthNames(getMonths());
        spanishI18n.setWeekdays(getWeekdays());
        spanishI18n.setWeekdaysShort(getWeekdaysShort());
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

    public static DateTimeFormatter presentationDateFormatter() {
        return DateTimeFormatter.ofPattern("dd MMM. yyyy");
    }

    public static DateTimeFormatter persistenceDateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public static NumberFormat puntuacionFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        return new DecimalFormat("0.0", symbols);
    }

    public static NumberFormat porcentajeFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("es"));
        return new DecimalFormat("00", symbols);
    }
}
