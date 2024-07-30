package com.barapp.web.model;

import com.barapp.web.data.entities.ConfiguradorHorarioEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HorarioPorRestaurante extends BaseModel {
    String idRestaurante;
    String correo;
    Map<String, ConfiguradorHorario> configuradores;
    List<Mesa> mesas;

    public List<ConfiguradorHorarioNoLaboral> getNoLaborales() {
        return configuradores.values().stream()
                .filter(c -> c instanceof ConfiguradorHorarioNoLaboral)
                .map(c -> (ConfiguradorHorarioNoLaboral) c)
                .toList();
    }

    public List<ConfiguradorHorario> getLaborales() {
        return configuradores.values().stream()
                .filter(c -> !(c instanceof ConfiguradorHorarioNoLaboral))
                .toList();
    }
}
