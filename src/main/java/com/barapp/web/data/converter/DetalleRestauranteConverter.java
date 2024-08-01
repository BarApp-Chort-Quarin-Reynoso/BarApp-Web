package com.barapp.web.data.converter;
import com.barapp.web.data.entities.DetalleRestauranteEntity;

import com.barapp.web.model.CalificacionPromedio;
import com.barapp.web.model.DetalleRestaurante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class DetalleRestauranteConverter implements BaseConverter<DetalleRestaurante, DetalleRestauranteEntity> {
    @Override
    public DetalleRestauranteEntity toEntity(DetalleRestaurante dto) {
        return DetalleRestauranteEntity.builder()
                .idDetalleRestaurante(dto.getId())
                .descripcion(dto.getDescripcion())
                .menu(dto.getMenu())
                .capacidadTotal(new ArrayList<>(dto.getCapacidadTotal()))
                .caracteristicas(dto.getCaracteristicas())
                .build();
    }

    @Override
    public DetalleRestaurante toDto(DetalleRestauranteEntity entity) {
        return DetalleRestaurante.builder()
                .id(entity.getIdDetalleRestaurante())
                .descripcion(entity.getDescripcion())
                .capacidadTotal(new LinkedHashSet<>(entity.getCapacidadTotal()))
                .menu(entity.getMenu())
                .caracteristicas(getCaracteristicasOrdenadas(entity.getCaracteristicas()))
                .build();
    }

    private Map<String, CalificacionPromedio> getCaracteristicasOrdenadas(Map<String, CalificacionPromedio> caracteristicas) {
        if (caracteristicas == null) {
            return Collections.emptyMap();
        }
        Map<String, CalificacionPromedio> ordenadas = new LinkedHashMap<>();
        caracteristicas.keySet().stream().sorted().forEach(k -> ordenadas.put(k, caracteristicas.get(k)));
        return ordenadas;
    }
}
