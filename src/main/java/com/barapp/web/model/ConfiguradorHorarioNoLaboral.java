package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.model.enums.TipoConfigurador;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfiguradorHorarioNoLaboral extends ConfiguradorHorario {
    LocalDate fecha;
    @Builder.Default
    Map<TipoComida, IntervaloTiempo> horarios = null;

    @Override
    public boolean isPermitido(LocalDate date) {
        return fecha.isEqual(date);
    }

    @Override
    public TipoConfigurador getTipo() {
        return TipoConfigurador.NO_LABORAL;
    }

    @Override
    public List<Horario> generarHorarios() {
        return List.of();
    }
}
