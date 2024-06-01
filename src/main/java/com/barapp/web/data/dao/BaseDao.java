package com.barapp.web.data.dao;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.BaseModel;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Filter;

import java.util.List;

public interface BaseDao<D extends BaseModel, E extends BaseEntity> {
    String save(D dto, String id) throws Exception;

    String save(D dto) throws Exception;

    void delete(String id) throws Exception;

    D get(String id) throws Exception;

    List<D> getAll() throws Exception;

    List<D> getFiltered(Filter... filters) throws Exception;

    CollectionReference getCollection();

    BaseConverter<D, E> getConverter();
}
