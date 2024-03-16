package com.barapp.web.business.impl;

import java.util.List;

import com.barapp.web.business.service.BaseService;
import com.barapp.web.model.BaseDto;

public abstract class BaseServiceImpl<D extends BaseDto> implements BaseService<D> {

    @Override
    public String save(D dto, String id) throws Exception {
	return getDao().save(dto, id);
    }

    @Override
    public String save(D dto) throws Exception {
	return getDao().save(dto);
    }

    @Override
    public void delete(String id) throws Exception {
	getDao().delete(id);	
    }

    @Override
    public D get(String id) throws Exception {
	return getDao().get(id);
    }

    @Override
    public List<D> getAll() throws Exception {
	return getDao().getAll();
    }
}
