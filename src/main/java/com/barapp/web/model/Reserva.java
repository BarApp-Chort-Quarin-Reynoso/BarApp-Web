package com.barapp.web.model;

import com.barapp.web.model.enums.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Reserva extends BaseModel {
    EstadoReserva estado;
    Integer cantidadPersonas;
    LocalDate fecha;
    Horario horario;
    UsuarioApp usuario;
    Restaurante restaurante;
    String idOpinion;

    @Override
    public String toString() {
        return "Reserva [id=" + id + ", estado=" + estado + ", cantidadPersonas=" + cantidadPersonas + ", fecha=" + fecha + ", horario=" + horario + ", usuario=" + usuario + ", restaurante=" + restaurante + ", idOpinion=" + idOpinion + "]";
    }
}
