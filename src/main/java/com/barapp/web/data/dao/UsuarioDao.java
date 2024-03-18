package com.barapp.web.data.dao;

import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.UsuarioDto;
import com.google.firebase.auth.FirebaseAuthException;

public interface UsuarioDao extends BaseDao<UsuarioDto, UsuarioEntity> {
    UsuarioDto buscarPorEmailEnFirebaseAuth(String email, String contrasenia) throws FirebaseAuthException;
    String crearUsuarioEnFirebaseAuth();
    
}
