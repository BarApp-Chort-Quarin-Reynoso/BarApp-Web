package com.barapp.web.model;

import com.barapp.web.model.enums.EstadoReserva;
import com.barapp.web.model.enums.TipoComida;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    TipoComida tipoComida;
    String motivoCancelacion;

    @Override
    public String toString() {
        return "Reserva [id=" + id + ", estado=" + estado + ", cantidadPersonas=" + cantidadPersonas + ", fecha=" + fecha + ", horario=" + horario + ", usuario=" + usuario + ", restaurante=" + restaurante + ", idOpinion=" + idOpinion + "]";
    }

    public LocalDateTime getFechaHora() {
        return LocalDateTime.of(fecha, horario.getHorario());
    }
}
