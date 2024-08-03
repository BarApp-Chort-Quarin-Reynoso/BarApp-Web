package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class HorarioConCapacidadDisponible {
    private List<LocalTime> horarios;
    private TipoComida tipoComida;
    private List<Mesa> mesas;
}
