package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfiguracionEntity extends BaseEntity {
    List<String> caracteristicas;
}
