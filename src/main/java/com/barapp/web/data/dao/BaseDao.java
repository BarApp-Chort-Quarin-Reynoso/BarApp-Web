package com.barapp.web.data.dao;

import java.util.List;

import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.BaseDto;

public interface BaseDao<D extends BaseDto, E extends BaseEntity> {
    String save(D dto, String id) throws Exception;
	
    String save(D dto) throws Exception;
	
    void delete(String id) throws Exception;
	
    D get(String id) throws Exception;
	
    List<D> getAll() throws Exception;
}
