package com.barapp.web.business.impl;

import org.springframework.stereotype.Service;

import com.barapp.web.business.service.UsuarioService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.UsuarioDao;
import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.UsuarioDto;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<UsuarioDto> implements UsuarioService {
    
    private final UsuarioDao usuarioDao;
    
    public UsuarioServiceImpl(UsuarioDao usuarioDao) {
	this.usuarioDao = usuarioDao;
    }

    @Override
    public BaseDao<UsuarioDto, UsuarioEntity> getDao() {
	return usuarioDao;
    }
}
