package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Horario extends BaseModel {
    private LocalTime horario;
    private TipoComida tipoComida;

    public Horario() {
        this.id = UUID.randomUUID().toString();
    }
}
