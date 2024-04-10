package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetalleRestauranteEntity extends BaseEntity {
    String idDetalleRestaurante;
    String descripcion;
    String menu;
    Integer capacidadPorHorario;
    List<HorarioEntity> listaHorarioEntities;
    List<OpinionUsuarioEntity> listaOpinionEntities;
}
