package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntervaloTiempo extends BaseModel {
    LocalTime desde;
    LocalTime hasta;
    int duracionReserva;

    public List<Horario> generarHorarios(TipoComida tipoComida) {
        List<Horario> retorno = new ArrayList<>();
        LocalTime it = desde;

        while (!it.plusMinutes(duracionReserva).isAfter(hasta)
                && !it.plusMinutes(duracionReserva).isBefore(desde)) {
            retorno.add(new Horario(it, tipoComida));
            it = it.plusMinutes(duracionReserva);
        }

        return retorno;
    }
}
