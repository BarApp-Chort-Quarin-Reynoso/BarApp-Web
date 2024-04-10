package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OpinionUsuarioEntity extends BaseEntity {
    String idOpinion;
    String comentario;
    Double nota;
    String idUsuario;
    String nombreUsuario;
    String apellidoUsuario;
    String fotoUsuario;
    String idDetalleUsuario;
}
