package com.barapp.web.business.impl;

import com.barapp.web.business.service.UsuarioService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.UsuarioDao;
import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.UsuarioApp;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<UsuarioApp> implements UsuarioService {

    private final UsuarioDao usuarioDao;

    public UsuarioServiceImpl(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    public BaseDao<UsuarioApp, UsuarioEntity> getDao() {
        return usuarioDao;
    }
}
