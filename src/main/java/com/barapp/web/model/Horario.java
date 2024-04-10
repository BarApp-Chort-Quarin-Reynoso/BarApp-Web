package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Horario extends BaseModel {
    LocalTime hora;
    TipoComida tipoComida;

    public Horario() {
        this.id = UUID.randomUUID().toString();
    }
}
