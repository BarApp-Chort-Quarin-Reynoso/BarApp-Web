package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Opinion extends BaseModel {
    String idRestaurante;
    String comentario;
    Double nota;
    LocalDate fecha;
    Horario horario;
    Integer cantidadPersonas;
    UsuarioApp usuario;
    Map<String, Integer> caracteristicas;

    public Opinion() {
        this.id = UUID.randomUUID().toString();
    }
}
