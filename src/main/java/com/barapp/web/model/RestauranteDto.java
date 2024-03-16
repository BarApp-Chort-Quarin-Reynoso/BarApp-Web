package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteDto extends BaseDto {
    private String nombre;
    private Double puntuacion;
    private String logo;
    private String foto;
    
    @Override
    public String toString() {
	return "RestauranteDto [id=" + id + " nombre=" + nombre + "]";
    }
}
