package com.barapp.web.model;

import com.barapp.web.model.enums.TipoComida;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HorarioPorRestaurante extends BaseModel {
    String idRestaurante;
    String correo;

    @Builder.Default
    Map<String, ConfiguradorHorario> configuradores = new LinkedHashMap<>();

    @Builder.Default
    List<Mesa> mesas = new ArrayList<>();

    public List<ConfiguradorHorarioNoLaboral> getNoLaborales() {
        return configuradores.values().stream()
                .filter(c -> c instanceof ConfiguradorHorarioNoLaboral)
                .map(c -> (ConfiguradorHorarioNoLaboral) c)
                .toList();
    }

    public List<ConfiguradorHorario> getLaborales() {
        return configuradores.values().stream()
                .filter(c -> !(c instanceof ConfiguradorHorarioNoLaboral))
                .toList();
    }

    public List<Reserva> filterReservasInvalidas(List<Reserva> reservas) {
        List<Reserva> reservasInvalidas = new ArrayList<>();

        Map<ConfiguradorHorario, Map<TipoComida, Set<Mesa>>> mesasPorConfigurador = new LinkedHashMap<>();
        configuradores.values().forEach(ch -> {
            mesasPorConfigurador.put(ch, new LinkedHashMap<>());
            ch.getCapacidadPorComida()
                    .forEach((key, value) -> mesasPorConfigurador.get(ch).put(key, value
                            .stream()
                            .map(Mesa::new).collect(Collectors.toSet())));
        });

        for (Reserva reserva : reservas) {
            for (ConfiguradorHorario ch : configuradores.values()) {
                if (ch.isPermitido(reserva.getFecha())) {
                    if (!ch.generarHorarios().contains(reserva.getHorario())) {
                        reservasInvalidas.add(reserva);
                    } else {
                        Optional<Mesa> mesaOpt = mesasPorConfigurador.get(ch).get(reserva.getHorario().getTipoComida()).stream()
                                .sorted(Comparator.comparing(Mesa::getCantidadDePersonasPorMesa))
                                .filter(m ->
                                        m.getCantidadDePersonasPorMesa() >= reserva.getCantidadPersonas()
                                        && m.getCantidadDePersonasPorMesa() != 0)
                                .findFirst();
                        if (mesaOpt.isEmpty()) {
                            reservasInvalidas.add(reserva);
                        } else {
                            mesaOpt.get().setCantidadMesas(mesaOpt.get().getCantidadMesas() - 1);
                        }
                    }

                    break;
                }
            }
        }

        return reservasInvalidas;
    }

    public boolean hayReservasInvalidadas(List<Reserva> reservas) {
        return true;
    }
}
