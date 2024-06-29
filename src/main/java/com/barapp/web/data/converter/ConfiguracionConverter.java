package com.barapp.web.data.converter;

import com.barapp.web.data.entities.ConfiguracionEntity;
import com.barapp.web.model.Configuracion;

public class ConfiguracionConverter implements BaseConverter<Configuracion, ConfiguracionEntity> {
    @Override
    public ConfiguracionEntity toEntity(Configuracion dto) {
        return ConfiguracionEntity.builder()
                .caracteristicas(dto.getCaracteristicas())
                .build();
    }

    @Override
    public Configuracion toDto(ConfiguracionEntity entity) {
        return Configuracion.builder()
                .caracteristicas(entity.getCaracteristicas())
                .build();
    }
}
