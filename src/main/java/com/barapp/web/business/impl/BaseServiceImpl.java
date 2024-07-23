package com.barapp.web.business.impl;

import com.barapp.web.business.service.BaseService;
import com.barapp.web.model.BaseModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BaseServiceImpl<D extends BaseModel> implements BaseService<D> {

    @Override
    @Transactional
    public String save(D dto, String id) throws Exception {
        return getDao().save(dto, id);
    }

    @Override
    @Transactional
    public String save(D dto) throws Exception {
        return getDao().save(dto);
    }

    @Override
    @Transactional
    public void delete(String id) throws Exception {
        getDao().delete(id);
    }

    @Override
    public D get(String id) throws Exception {
        return getDao().get(id);
    }

    @Override
    public List<D> getAll() {
        return getDao().getAll();
    }

    @Override
    public List<D> getAll(Set<Entry<String, String>> allParams) throws Exception {
        return getDao().getAll(allParams);
    }
}
