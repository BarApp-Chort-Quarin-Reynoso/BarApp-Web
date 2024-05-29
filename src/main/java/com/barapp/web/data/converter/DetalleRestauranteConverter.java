package com.barapp.web.data.converter;
import com.barapp.web.data.entities.DetalleRestauranteEntity;

import com.barapp.web.model.DetalleRestaurante;

import java.util.ArrayList;
import java.util.LinkedHashSet;

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
                .caracteristicas(entity.getCaracteristicas())
                .build();
    }
}
