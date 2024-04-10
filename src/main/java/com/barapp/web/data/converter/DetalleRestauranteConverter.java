package com.barapp.web.data.converter;

import com.barapp.web.data.entities.DetalleRestauranteEntity;
import com.barapp.web.model.DetalleRestaurante;

public class DetalleRestauranteConverter implements BaseConverter<DetalleRestaurante, DetalleRestauranteEntity> {
    @Override
    public DetalleRestauranteEntity toEntity(DetalleRestaurante dto) {
        DetalleRestauranteEntity entity = DetalleRestauranteEntity.builder()
                                                                  .idDetalleRestaurante(dto.getId())
                                                                  .descripcion(dto.getDescripcion())
                                                                  .menu(dto.getMenu())
                                                                  .capacidadPorHorario(dto.getCapacidadPorHorario())
                                                                  .listaHorarioEntities(HorarioConverter.toEntityList(dto.getListaHorarios()))
                                                                  .listaOpinionEntities(OpinionConverter.toEntityList(dto.getListaOpiniones()))
                                                                  .build();

        return entity;
    }

    @Override
    public DetalleRestaurante toDto(DetalleRestauranteEntity entity) {
        DetalleRestaurante detalleRestaurante = DetalleRestaurante.builder()
                                                                  .capacidadPorHorario(entity.getCapacidadPorHorario())
                                                                  .descripcion(entity.getDescripcion())
                                                                  .menu(entity.getMenu())
                                                                  .listaHorarios(HorarioConverter.toModelList(entity.getListaHorarioEntities()))
                                                                  .listaOpiniones(OpinionConverter.toModelList(entity.getListaOpinionEntities()))
                                                                  .build();

        detalleRestaurante.setId(entity.getIdDetalleRestaurante());
        return detalleRestaurante;
    }
}
