package com.barapp.web.data.converter;

import com.barapp.web.data.entities.EstadisticaEntity;
import com.barapp.web.model.Estadistica;
import com.barapp.web.model.enums.TipoComida;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaConverter implements BaseConverter<Estadistica, EstadisticaEntity> {
    @Override
    public EstadisticaEntity toEntity(Estadistica dto) {
        return EstadisticaEntity.builder()
                .idRestaurante(dto.getIdRestaurante())
                .correo(dto.getCorreo())
                .reservasConcretadas(dto.getReservasConcretadas())
                .diasActivo(dto.getDiasActivo())
                .clientesAtendidos(dto.getClientesAtendidos())
                .porcentajeOcupacionxTipoComida(dto.getPorcentajeOcupacionxTipoComida().entrySet()
                        .stream()
                        .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue)))
                .porcentajeOcupacionxDiaSemana(dto.getPorcentajeOcupacionxDiaSemana().entrySet()
                        .stream()
                        .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue)))
                .build();
    }

    @Override
    public Estadistica toDto(EstadisticaEntity entity) {
        return Estadistica.builder()
                .idRestaurante(entity.getIdRestaurante())
                .correo(entity.getCorreo())
                .reservasConcretadas(entity.getReservasConcretadas())
                .diasActivo(entity.getDiasActivo())
                .clientesAtendidos(entity.getClientesAtendidos())
                .porcentajeOcupacionxTipoComida(entity.getPorcentajeOcupacionxTipoComida().entrySet()
                        .stream()
                        .collect(Collectors.toMap(e -> TipoComida.valueOf(e.getKey()), Map.Entry::getValue)))
                .porcentajeOcupacionxDiaSemana(entity.getPorcentajeOcupacionxDiaSemana().entrySet()
                        .stream()
                        .collect(Collectors.toMap(e -> DayOfWeek.valueOf(e.getKey()), Map.Entry::getValue)))
                .build();
    }
}
