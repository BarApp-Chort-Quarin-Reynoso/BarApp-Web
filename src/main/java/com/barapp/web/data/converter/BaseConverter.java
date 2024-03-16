package com.barapp.web.data.converter;

import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.BaseDto;

public interface BaseConverter<D extends BaseDto, E extends BaseEntity> {
    E toEntity(D dto);
    D toDto(E entity);
}
