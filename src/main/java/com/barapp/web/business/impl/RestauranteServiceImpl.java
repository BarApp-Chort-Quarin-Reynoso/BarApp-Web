package com.barapp.web.business.impl;

import org.springframework.stereotype.Service;

import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.RestauranteDao;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.RestauranteDto;

@Service
public class RestauranteServiceImpl extends BaseServiceImpl<RestauranteDto> implements RestauranteService {
    
    private final RestauranteDao restauranteDao;
    
    public RestauranteServiceImpl(RestauranteDao restauranteDao) {
	this.restauranteDao = restauranteDao;
    }

    @Override
    public BaseDao<RestauranteDto, RestauranteEntity> getDao() {
	return restauranteDao;
    }

}
