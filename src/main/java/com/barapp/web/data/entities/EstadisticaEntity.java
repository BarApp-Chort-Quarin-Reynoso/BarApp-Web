package com.barapp.web.data.entities;

import com.barapp.web.model.enums.TipoComida;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EstadisticaEntity extends BaseEntity {
    String idRestaurante;
    String correo;
    int reservasConcretadas;
    int diasActivo;
    int clientesAtendidos;
    @Builder.Default
    Map<String, Double> porcentajeOcupacionxTipoComida = new HashMap<>();
    @Builder.Default
    Map<String, Double> porcentajeOcupacionxDiaSemana = new HashMap<>();
}
