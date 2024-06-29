package com.barapp.web.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PROTECTED)
public class CalificacionPromedio extends BaseModel {
    Double puntuacion;
    Integer cantidadOpiniones;
}
