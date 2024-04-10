package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Opinion extends BaseModel {
    String comentario;
    Double nota;
    UsuarioMobileDto usuario;

    public Opinion() {
        this.id = UUID.randomUUID().toString();
    }
}
