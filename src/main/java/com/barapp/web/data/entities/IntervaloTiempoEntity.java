package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
}
