package com.barapp.web.data.impl;

import org.springframework.stereotype.Service;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.UsuarioConverter;
import com.barapp.web.data.dao.UsuarioDao;
import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.UsuarioDto;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;

@Service
public class UsuarioDaoImpl extends BaseDaoImpl<UsuarioDto, UsuarioEntity> implements UsuarioDao {

    private final Firestore firestore;
    
    public UsuarioDaoImpl(Firestore firestore) {
	super(UsuarioEntity.class);
	
	this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
	return firestore.collection("usuarios");
    }

    @Override
    public BaseConverter<UsuarioDto, UsuarioEntity> getConverter() {
	return new UsuarioConverter();
    }

}
