package com.barapp.web.business.service;

import java.util.List;

import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.BaseDto;

public interface BaseService<D extends BaseDto> {
    String save(D dto, String id) throws Exception;
	
    String save(D dto) throws Exception;
	
    void delete(String id) throws Exception;
	
    D get(String id) throws Exception;
	
    List<D> getAll() throws Exception;
    
    BaseDao<D, ? extends BaseEntity> getDao() throws Exception;
}
