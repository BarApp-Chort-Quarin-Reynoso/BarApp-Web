package com.barapp.web.data.converter;

import com.barapp.web.data.entities.HorarioPorRestauranteEntity;
import com.barapp.web.model.HorarioPorRestaurante;

import java.util.Map;
import java.util.stream.Collectors;

public class HorarioPorRestauranteConverter implements BaseConverter<HorarioPorRestaurante, HorarioPorRestauranteEntity> {
    private final ConfiguradorHorarioConverter chConverter = new ConfiguradorHorarioConverter();

    @Override
    public HorarioPorRestauranteEntity toEntity(HorarioPorRestaurante dto) {
        ConfiguradorHorarioConverter chConverter = new ConfiguradorHorarioConverter();
        return HorarioPorRestauranteEntity.builder()
                .idRestaurante(dto.getIdRestaurante())
                .correo(dto.getCorreo())
                .configuradores(dto
                        .getConfiguradores()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> chConverter.toEntity(entry.getValue()))))
                .mesas(dto.getMesas())
                .build();
    }

    @Override
    public HorarioPorRestaurante toDto(HorarioPorRestauranteEntity entity) {
        return HorarioPorRestaurante.builder()
                .idRestaurante(entity.getIdRestaurante())
                .correo(entity.getCorreo())
                .configuradores(entity
                        .getConfiguradores()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                entry -> chConverter.toDtoWithId(entry.getValue(), entry.getKey())
                        )))
                .mesas(entity.getMesas())
                .build();
    }
}
