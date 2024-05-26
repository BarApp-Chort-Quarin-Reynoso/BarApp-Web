package com.barapp.web.utils;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FormatUtils {
    public static DatePicker.DatePickerI18n datePickerI18n() {
        DatePicker.DatePickerI18n spanishI18n = new DatePicker.DatePickerI18n();
        spanishI18n.setMonthNames(List.of("Enero", "Febrero", "Marzo", "Abril",
                "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre",
                "Noviembre", "Diciembre"));
        spanishI18n.setWeekdays(List.of("Domingo", "Lunes", "Martes",
                "Miércoles", "Jueves", "Viernes", "Sábado"));
        spanishI18n.setWeekdaysShort(
                List.of("Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa"));
        spanishI18n.setToday("Hoy");
        spanishI18n.setCancel("Cancelar");

        return spanishI18n;
    }

    public static DateTimeFormatter timeFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    public static DateTimeFormatter dateFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
}
