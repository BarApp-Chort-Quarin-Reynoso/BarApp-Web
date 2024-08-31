package com.barapp.web.data.converter;

import com.barapp.web.data.entities.ConfiguradorHorarioEntity;
import com.barapp.web.data.entities.HorarioPorRestauranteEntity;
import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.HorarioPorRestaurante;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class HorarioPorRestauranteConverter implements BaseConverter<HorarioPorRestaurante, HorarioPorRestauranteEntity> {
    private final ConfiguradorHorarioConverter chConverter = new ConfiguradorHorarioConverter();

    @Override
    public HorarioPorRestauranteEntity toEntity(HorarioPorRestaurante dto) {
        Map<String, ConfiguradorHorarioEntity> configuradoresOrdenados = new LinkedHashMap<>();
        dto.getConfiguradores().entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getValue().getTipo()))
                .forEach(e -> {
                    configuradoresOrdenados.put(e.getKey(), chConverter.toEntity(e.getValue()));
                });

        return HorarioPorRestauranteEntity.builder()
                .idRestaurante(dto.getIdRestaurante())
                .correo(dto.getCorreo())
                .configuradores(configuradoresOrdenados)
                .mesas(dto.getMesas())
                .build();
    }

    @Override
    public HorarioPorRestaurante toDto(HorarioPorRestauranteEntity entity) {
        Map<String, ConfiguradorHorario> configuradoresOrdenados = new LinkedHashMap<>();
        entity.getConfiguradores().entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getValue().getTipo()))
                .forEach(e -> {
                    ConfiguradorHorario ch = chConverter.toDtoWithId(e.getValue(), e.getKey());
                    configuradoresOrdenados.put(ch.getId(), ch);
                });

        return HorarioPorRestaurante.builder()
                .idRestaurante(entity.getIdRestaurante())
                .correo(entity.getCorreo())
                .configuradores(configuradoresOrdenados)
                .mesas(entity.getMesas())
                .build();
    }
}
