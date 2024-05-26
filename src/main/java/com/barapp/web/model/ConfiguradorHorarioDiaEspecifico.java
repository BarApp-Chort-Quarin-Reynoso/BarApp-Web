package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.model.enums.TipoConfigurador;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfiguradorHorarioDiaEspecifico extends ConfiguradorHorario {
    LocalDate fecha;

    @Override
    public boolean isPermitido(LocalDate date) {
        return fecha.isEqual(date);
    }

    @Override
    public TipoConfigurador getTipo() {
        return TipoConfigurador.DIA_ESPECIFICO;
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
