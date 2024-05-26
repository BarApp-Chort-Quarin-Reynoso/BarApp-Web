package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.model.enums.TipoConfigurador;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfiguradorHorarioSemanal extends ConfiguradorHorario {
    @Builder.Default
    Set<DayOfWeek> daysOfWeek = new HashSet<>();

    @Override
    public boolean isPermitido(LocalDate date) {
        return daysOfWeek.contains(date.getDayOfWeek());
    }

    @Override
    public TipoConfigurador getTipo() {
        return TipoConfigurador.SEMANAL;
    }

    @Override
    public List<Horario> generarHorarios() {
        List<Horario> retorno = new ArrayList<>();
        for (TipoComida tipoComida : TipoComida.values()) {
            if (horarios.containsKey(tipoComida)) {
                retorno.addAll(horarios.get(tipoComida).generarHorarios(tipoComida));
            }
        }

        return retorno;
    }
}
