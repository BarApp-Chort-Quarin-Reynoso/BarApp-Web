package com.barapp.web.data.converter;

import com.barapp.web.data.entities.ConfiguradorHorarioEntity;
import com.barapp.web.data.entities.IntervaloTiempoEntity;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.model.enums.TipoConfigurador;
import com.barapp.web.utils.FormatUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfiguradorHorarioConverter implements BaseConverter<ConfiguradorHorario, ConfiguradorHorarioEntity> {
    @Override
    public ConfiguradorHorarioEntity toEntity(ConfiguradorHorario dto) {
        ConfiguradorHorarioEntity.ConfiguradorHorarioEntityBuilder builder = ConfiguradorHorarioEntity.builder();

        if (dto instanceof ConfiguradorHorarioSemanal semanal) {
            builder.tipo(TipoConfigurador.SEMANAL.getOrden());
            builder.daysOfWeek(semanal.getDaysOfWeek().stream().map(DayOfWeek::toString).toList());
            builder.horarios(toHorariosEntity(dto.getHorarios()));
        } else if (dto instanceof ConfiguradorHorarioDiaEspecifico especifico) {
            builder.tipo(TipoConfigurador.DIA_ESPECIFICO.getOrden());
            builder.fecha(especifico.getFecha().format(FormatUtils.persistenceDateFormatter()));
            builder.horarios(toHorariosEntity(dto.getHorarios()));
        } else if (dto instanceof ConfiguradorHorarioNoLaboral noLaboral) {
            builder.tipo(TipoConfigurador.NO_LABORAL.getOrden());
            builder.fecha(noLaboral.getFecha().format(FormatUtils.persistenceDateFormatter()));
        }

        return builder.build();
    }

    @Override
    public ConfiguradorHorario toDto(ConfiguradorHorarioEntity entity) {
        ConfiguradorHorario dto;

        if (entity.getTipo() == TipoConfigurador.SEMANAL.getOrden()) {
            dto = ConfiguradorHorarioSemanal.builder()
                    .daysOfWeek(entity.getDaysOfWeek().stream().map(DayOfWeek::valueOf).collect(Collectors.toSet()))
                    .horarios(toHorariosDto(entity.getHorarios()))
                    .build();
        } else if (entity.getTipo() == TipoConfigurador.DIA_ESPECIFICO.getOrden()) {
            dto = ConfiguradorHorarioDiaEspecifico.builder()
                    .fecha(LocalDate.parse(entity.getFecha(), FormatUtils.persistenceDateFormatter()))
                    .horarios(toHorariosDto(entity.getHorarios()))
                    .build();
        } else if (entity.getTipo() == TipoConfigurador.NO_LABORAL.getOrden()) {
            dto = ConfiguradorHorarioNoLaboral.builder()
                    .fecha(LocalDate.parse(entity.getFecha(), FormatUtils.persistenceDateFormatter()))
                    .build();
        } else {
            throw new IllegalArgumentException("La entidad asociada no tiene un tipo asignado soportado");
        }

        return dto;
    }

    public ConfiguradorHorario toDtoWithId(ConfiguradorHorarioEntity entity, String id) {
        ConfiguradorHorario dto = toDto(entity);
        dto.setId(id);
        return dto;
    }

    private Map<String, IntervaloTiempoEntity> toHorariosEntity(Map<TipoComida, IntervaloTiempo> horariosDto) {
        Map<String, IntervaloTiempoEntity> horariosEntity = new HashMap<>();

        horariosDto.forEach(((tipoComida, intervaloTiempo) -> horariosEntity.put(
                tipoComida.toString(),
                IntervaloTiempoEntity.builder()
                        .desde(intervaloTiempo.getDesde().format(FormatUtils.timeFormatter()))
                        .hasta(intervaloTiempo.getHasta().format(FormatUtils.timeFormatter()))
                        .horarios(intervaloTiempo
                                .getHorarios()
                                .stream()
                                .map(horario -> horario.format(FormatUtils.timeFormatter()))
                                .collect(Collectors.toList()))
                        .build()
        )));

        return horariosEntity;
    }

    private Map<TipoComida, IntervaloTiempo> toHorariosDto(Map<String, IntervaloTiempoEntity> horariosEntity) {
        Map<TipoComida, IntervaloTiempo> horariosDto = new HashMap<>();

        horariosEntity.forEach((tipoComida, intervaloTiempo) -> horariosDto.put(
                TipoComida.valueOf(tipoComida),
                IntervaloTiempo.builder()
                        .desde(LocalTime.parse(intervaloTiempo.getDesde(), FormatUtils.timeFormatter()))
                        .hasta(LocalTime.parse(intervaloTiempo.getHasta(), FormatUtils.timeFormatter()))
                        .horarios(intervaloTiempo
                                .getHorarios()
                                .stream()
                                .map(horario -> LocalTime.parse(horario, FormatUtils.timeFormatter()))
                                .collect(Collectors.toList()))
                        .build()
        ));

        return horariosDto;
    }
}
