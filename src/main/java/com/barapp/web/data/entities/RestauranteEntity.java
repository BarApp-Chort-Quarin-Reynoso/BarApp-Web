package com.barapp.web.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteEntity extends BaseEntity {
    private String nombre;
    private Double puntuacion;
    private String logo;
    private String foto;
}
