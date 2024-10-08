package com.barapp.web.data.entities;

import com.barapp.web.model.CalificacionPromedio;
import com.barapp.web.model.Mesa;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetalleRestauranteEntity extends BaseEntity {
    String idRestaurante;
    String idDetalleRestaurante;
    String descripcion;
    String menu;
    List<Mesa> capacidadTotal;
    Map<String, CalificacionPromedio> caracteristicas;
}
