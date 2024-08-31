package com.barapp.web.model;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestauranteDto extends BaseModel {
    private String nombre;
    private String correo;
    private Double puntuacion;
    private String portada;
    private String logo;
    private String telefono;
    private String cuit;
    private Ubicacion ubicacion;
    private String idDetalleRestaurante;
    private Optional<DetalleRestaurante> detalleRestaurante;

    @Override
    public String toString() {
        return "RestauranteDto [id=" + id + ", nombre=" + nombre + ", correo=" + correo + ", puntuacion=" + puntuacion
                + ", portada=" + portada + ", logo=" + logo + ", telefono=" + telefono + ", cuit=" + cuit + ", ubicacion="
                + ubicacion + ", idDetalleRestaurante=" + idDetalleRestaurante + ", detalleRestaurante=" + detalleRestaurante
                + "]";
    }
}
