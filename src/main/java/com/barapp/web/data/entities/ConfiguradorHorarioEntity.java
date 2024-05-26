package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfiguradorHorarioEntity extends BaseEntity {
    String correoRestaurante;
    @Builder.Default
    Integer tipo = 0;
    @Builder.Default
    List<String> daysOfWeek = null;     // Se utiliza para ConfiguradorHorarioSemanal
    @Builder.Default
    String fecha = null;     // Se utiliza para ConfiguradorHorarioDiaEspecifico y ConfiguradorHorarioDiaInhabilitado
    @Builder.Default
    Map<String, IntervaloTiempoEntity> horarios = new HashMap<>();
}
