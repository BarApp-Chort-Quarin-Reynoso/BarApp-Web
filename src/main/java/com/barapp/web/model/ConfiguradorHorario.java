package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.model.enums.TipoConfigurador;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class ConfiguradorHorario extends BaseModel {
    @Builder.Default
    Map<TipoComida, IntervaloTiempo> horarios = new LinkedHashMap<>();

    public abstract boolean isPermitido(LocalDate date);

    public abstract TipoConfigurador getTipo();

    public abstract List<Horario> generarHorarios();
}
