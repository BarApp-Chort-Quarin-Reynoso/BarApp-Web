package com.barapp.web.business.impl;

import com.barapp.web.business.service.UsuarioService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.DetalleUsuarioDao;
import com.barapp.web.data.dao.UsuarioDao;
import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.DetalleUsuario;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioApp;
import com.google.cloud.firestore.Filter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<UsuarioApp> implements UsuarioService {

    private final UsuarioDao usuarioDao;
    private final DetalleUsuarioDao detalleUsuarioDao;

    public UsuarioServiceImpl(UsuarioDao usuarioDao, DetalleUsuarioDao detalleUsuarioDao) {
        this.usuarioDao = usuarioDao;
        this.detalleUsuarioDao = detalleUsuarioDao;
    }

    @Override
    public BaseDao<UsuarioApp, UsuarioEntity> getDao() {
        return usuarioDao;
    }

    @Override
    public Optional<UsuarioApp> getByMail(String mail) {
        try {
            List<DetalleUsuario> detallesUsuarios = detalleUsuarioDao.getFiltered(Filter.equalTo("mail", mail));
  
            if (detallesUsuarios.isEmpty()) return Optional.empty();

            List<UsuarioApp> usuarios = usuarioDao.getFiltered(Filter.equalTo("idDetalleUsuario", detallesUsuarios.get(0).getId()));

            if (usuarios.isEmpty()) return Optional.empty();
            
            return Optional.of(usuarios.get(0));
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
