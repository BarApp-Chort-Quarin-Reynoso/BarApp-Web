package com.barapp.web.data.converter;

import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.BaseModel;

public interface BaseConverter<D extends BaseModel, E extends BaseEntity> {
    E toEntity(D dto);

    D toDto(E entity);
}
