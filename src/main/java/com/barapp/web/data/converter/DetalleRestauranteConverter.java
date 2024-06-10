package com.barapp.web.data.converter;

import com.barapp.web.data.entities.DetalleRestauranteEntity;
import com.barapp.web.model.DetalleRestaurante;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class DetalleRestauranteConverter implements BaseConverter<DetalleRestaurante, DetalleRestauranteEntity> {
    @Override
    public DetalleRestauranteEntity toEntity(DetalleRestaurante dto) {
        DetalleRestauranteEntity entity = DetalleRestauranteEntity.builder()
                                                                  .idDetalleRestaurante(dto.getId())
                                                                  .descripcion(dto.getDescripcion())
                                                                  .menu(dto.getMenu())
                                                                  .capacidadTotal(new ArrayList<>(dto.getCapacidadTotal()))
                                                                  .listaHorarioEntities(HorarioConverter.toEntityList(dto.getListaHorarios()))
                                                                  .listaOpinionEntities(OpinionConverter.toEntityList(dto.getListaOpiniones()))
                                                                  .build();

        return entity;
    }

    @Override
    public DetalleRestaurante toDto(DetalleRestauranteEntity entity) {
        DetalleRestaurante detalleRestaurante = DetalleRestaurante.builder()
                                                                  .descripcion(entity.getDescripcion())
                                                                  .menu(entity.getMenu())
                                                                  .capacidadTotal(new LinkedHashSet<>(entity.getCapacidadTotal()))
                                                                  .listaHorarios(HorarioConverter.toModelList(entity.getListaHorarioEntities()))
                                                                  .listaOpiniones(OpinionConverter.toModelList(entity.getListaOpinionEntities()))
                                                                  .build();

        detalleRestaurante.setId(entity.getIdDetalleRestaurante());
        return detalleRestaurante;
    }
}
