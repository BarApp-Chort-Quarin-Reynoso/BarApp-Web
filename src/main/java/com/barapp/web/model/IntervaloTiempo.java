package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
    int duracionReserva = 15;
    List<LocalTime> horarios = new ArrayList<>();

    public List<Horario> generarHorarios(TipoComida tipoComida) {
        return horarios.stream().map(hora -> {
            Horario horario = Horario.builder()
                    .tipoComida(tipoComida)
                    .horario(hora)
                    .build();
            return horario;
        }).toList();
    }
}
