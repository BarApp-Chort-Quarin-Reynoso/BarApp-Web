package com.barapp.web.data.entities;

import com.barapp.web.model.Mesa;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntervaloTiempoEntity extends BaseEntity {
    String desde;
    String hasta;
    List<String> horarios;
    List<Mesa> mesas;
}
