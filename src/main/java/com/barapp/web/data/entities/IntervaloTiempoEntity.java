package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntervaloTiempoEntity extends BaseEntity  {
    String desde;
    String hasta;
    Integer duracion;
    List<String> horarios;
}
