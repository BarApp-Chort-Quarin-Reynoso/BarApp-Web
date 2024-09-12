package com.barapp.web.business.impl;

import com.barapp.web.business.service.BaseService;
import com.barapp.web.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BaseServiceImpl<D extends BaseModel> implements BaseService<D> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String save(D dto, String id) throws Exception {
        String retorno = getDao().save(dto, id);
        logger.info("Se ha guardado el objeto de tipo {} con id {}", dto.getClass(), id);
        return retorno;
    }

    @Override
    public String save(D dto) throws Exception {
        String id = getDao().save(dto);
        logger.info("Se ha guardado el objeto de tipo {} con id {}", dto.getClass(), id);
        return id;
    }

    @Override
    public void delete(String id) throws Exception {
        getDao().delete(id);
        logger.info("Se ha eliminado el objeto con id {}", id);
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
