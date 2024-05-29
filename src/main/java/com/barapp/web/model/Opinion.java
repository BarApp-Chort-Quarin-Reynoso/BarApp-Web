package com.barapp.web.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
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
    UsuarioApp usuario;

    public Opinion() {
        this.id = UUID.randomUUID().toString();
    }
}
