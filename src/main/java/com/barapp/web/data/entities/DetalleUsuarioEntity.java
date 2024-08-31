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
public class DetalleUsuarioEntity extends BaseEntity {
    List<String> busquedasRecientes;
    String mail;
    List<String> idsRestaurantesFavoritos;
    String telefono;
}
