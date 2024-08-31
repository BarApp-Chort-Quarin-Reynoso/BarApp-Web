package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntervaloTiempo extends BaseModel {
    LocalTime desde;
    LocalTime hasta;

    @Builder.Default
    List<LocalTime> horarios = new ArrayList<>();

    @Builder.Default
    List<Mesa> mesas = new ArrayList<>();

    public List<Horario> generarHorarios(TipoComida tipoComida) {
        return horarios.stream().map(hora -> {
            Horario horario = Horario.builder()
                    .tipoComida(tipoComida)
                    .horario(hora)
                    .build();
            return horario;
        }).toList();
    }

    public int getCapacidadTotal() {
        return mesas.stream()
                .map(mesa -> mesa.getCantidadMesas() * mesa.getCantidadDePersonasPorMesa())
                .reduce(0, Integer::sum);
    }
}
