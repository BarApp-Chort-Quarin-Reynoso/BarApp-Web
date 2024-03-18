package com.barapp.web.data.impl;

import org.springframework.stereotype.Service;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.UsuarioConverter;
import com.barapp.web.data.dao.UsuarioDao;
import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.UsuarioDto;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

@Service
public class UsuarioDaoImpl extends BaseDaoImpl<UsuarioDto, UsuarioEntity> implements UsuarioDao {

    private final Firestore firestore;
    private final FirebaseAuth firebaseAuth;
    
    public UsuarioDaoImpl(Firestore firestore, FirebaseAuth firebaseAuth) {
	super(UsuarioEntity.class);
	
	this.firestore = firestore;
	this.firebaseAuth = firebaseAuth;
    }

    @Override
    public CollectionReference getCollection() {
	return firestore.collection("usuarios");
    }

    @Override
    public BaseConverter<UsuarioDto, UsuarioEntity> getConverter() {
	return new UsuarioConverter();
    }

    @Override
    public UsuarioDto buscarPorEmailEnFirebaseAuth(String email, String contrasenia) throws FirebaseAuthException {
	firebaseAuth.getUserByEmail(contrasenia);
	return null;
    }

    @Override
    public String crearUsuarioEnFirebaseAuth() {
	// TODO Auto-generated method stub
	return null;
    }
}
