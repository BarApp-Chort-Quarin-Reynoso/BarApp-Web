package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OpinionEntity extends BaseEntity {
    String comentario;
    Double nota;
    String idUsuario;
    String idRestaurante;
    String nombreUsuario;
    String apellidoUsuario;
    String foto;
    String fecha;
}
