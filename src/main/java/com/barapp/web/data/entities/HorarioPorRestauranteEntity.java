package com.barapp.web.data.entities;

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
public class HorarioPorRestauranteEntity extends BaseEntity {
    String idRestaurante;
    String correo;
    Map<String, ConfiguradorHorarioEntity> configuradores;
}
