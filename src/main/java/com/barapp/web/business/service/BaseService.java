package com.barapp.web.business.service;

import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.BaseModel;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public interface BaseService<D extends BaseModel> {
    String save(D dto, String id) throws Exception;

    String save(D dto) throws Exception;

    void delete(String id) throws Exception;

    D get(String id) throws Exception;

    List<D> getAll(Set<Entry<String, String>> allParams) throws Exception;

    BaseDao<D, ? extends BaseEntity> getDao() throws Exception;
}
