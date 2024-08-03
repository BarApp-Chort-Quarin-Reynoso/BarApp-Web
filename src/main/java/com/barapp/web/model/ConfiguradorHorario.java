package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.model.enums.TipoConfigurador;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class ConfiguradorHorario extends BaseModel {
    @Builder.Default
    Map<TipoComida, IntervaloTiempo> horarios = new LinkedHashMap<>();

    @Builder.Default
    List<Mesa> mesas = new ArrayList<>();

    public abstract boolean isPermitido(LocalDate date);

    public abstract TipoConfigurador getTipo();

    public abstract List<Horario> generarHorarios();

    public Map<TipoComida, Set<Mesa>> getCapacidadPorComida() {
        Map<TipoComida, Set<Mesa>> capacidadPorComida = new LinkedHashMap<>();
        for (TipoComida tipoComida : TipoComida.values()) {
            IntervaloTiempo horario = horarios.get(tipoComida);
            if (horario == null) {
                capacidadPorComida.put(tipoComida, new LinkedHashSet<>());
            } else {
                List<Mesa> capacidad = horario.getMesas() != null && !horario
                        .getMesas()
                        .isEmpty() ? horario.getMesas() : mesas;
                capacidadPorComida.put(tipoComida, new LinkedHashSet<>(capacidad));
            }
        }

        return capacidadPorComida;
    }

    public boolean tieneHorariosParaTipoComida(TipoComida tipoComida) {
        return horarios.containsKey(tipoComida);
    }
}
