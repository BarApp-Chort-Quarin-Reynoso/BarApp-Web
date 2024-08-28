package com.barapp.web.business.impl;

import com.barapp.web.business.service.UsuarioService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.DetalleUsuarioDao;
import com.barapp.web.data.dao.RestauranteFavoritoDao;
import com.barapp.web.data.dao.RestauranteVistoRecientementeDao;
import com.barapp.web.data.dao.UsuarioDao;
import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.DetalleUsuario;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.UsuarioApp;
import com.google.cloud.firestore.Filter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<UsuarioApp> implements UsuarioService {

    private final UsuarioDao usuarioDao;
    private final DetalleUsuarioDao detalleUsuarioDao;
    private final RestauranteFavoritoDao restauranteFavoritoDao;
    private final RestauranteVistoRecientementeDao restauranteVistoRecientementeDao;

    public UsuarioServiceImpl(UsuarioDao usuarioDao, DetalleUsuarioDao detalleUsuarioDao, RestauranteFavoritoDao restauranteFavoritoDao, RestauranteVistoRecientementeDao restauranteVistoRecientementeDao) {
        this.usuarioDao = usuarioDao;
        this.detalleUsuarioDao = detalleUsuarioDao;
        this.restauranteFavoritoDao = restauranteFavoritoDao;
        this.restauranteVistoRecientementeDao = restauranteVistoRecientementeDao;
    }

    @Override
    public BaseDao<UsuarioApp, UsuarioEntity> getDao() { return usuarioDao; }

    @Override
    public Optional<UsuarioApp> getByMail(String mail) {
        try {
            List<DetalleUsuario> detallesUsuarios = detalleUsuarioDao.getFiltered(Filter.equalTo("mail", mail));

            if (detallesUsuarios.isEmpty()) return Optional.empty();

            List<UsuarioApp> usuarios = usuarioDao
                .getFiltered(Filter.equalTo("idDetalleUsuario", detallesUsuarios.get(0).getId()));

            if (usuarios.isEmpty()) return Optional.empty();

            return Optional.of(usuarios.get(0));
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<DetalleUsuario> getUserDetail(String id) {
        try {
            return Optional.ofNullable(detalleUsuarioDao.get(id));
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String addUserDetail(String id, DetalleUsuario detalleUsuario) {
        try {
            return detalleUsuarioDao.save(detalleUsuario, id);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RestauranteUsuario> getFavoritos(String userId) {
        try {
            UsuarioApp usuario = this.get(userId);
            if (usuario == null) return null;

            return restauranteFavoritoDao.getByUserId(userId);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RestauranteUsuario> getVistosRecientemente(String userId) {
        try {
            UsuarioApp usuario = this.get(userId);
            if (usuario == null) return null;

            return restauranteVistoRecientementeDao.getByUserId(userId);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateFoto(String id, String foto) {
        try {
            UsuarioApp usuario = this.get(id);
            usuario.setFoto(foto);
            usuarioDao.save(usuario);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<DetalleUsuario> updateBusquedasRecientes(String id, List<String> busquedasRecientes) {
        try {
            return Optional.ofNullable(detalleUsuarioDao.updateBusquedasRecientes(id, busquedasRecientes));
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String registrarUsuario(String mail, String contrasenia) {
        try {
            return usuarioDao.registrarUsuario(mail, contrasenia);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
