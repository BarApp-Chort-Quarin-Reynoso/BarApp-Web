package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Estadistica extends BaseModel {
    String idRestaurante;
    String correo;
    int reservasConcretadas;
    int diasActivo;
    int clientesAtendidos;
    @Builder.Default
    Map<TipoComida, Double> porcentajeOcupacionxTipoComida = new HashMap<>();
    @Builder.Default
    Map<DayOfWeek, Double> porcentajeOcupacionxDiaSemana = new HashMap<>();
}
