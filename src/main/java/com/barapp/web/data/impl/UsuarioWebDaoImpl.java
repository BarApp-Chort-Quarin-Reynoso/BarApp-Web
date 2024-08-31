package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.UsuarioWebConverter;
import com.barapp.web.data.dao.UsuarioWebDao;
import com.barapp.web.data.entities.UsuarioWebEntity;
import com.barapp.web.model.UsuarioWeb;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioWebDaoImpl extends BaseDaoImpl<UsuarioWeb, UsuarioWebEntity> implements UsuarioWebDao {

    private final Firestore firestore;

    public UsuarioWebDaoImpl(Firestore firestore) {
        super(UsuarioWebEntity.class);

        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("usuariosWeb");
    }

    @Override
    public BaseConverter<UsuarioWeb, UsuarioWebEntity> getConverter() {
        return new UsuarioWebConverter();
    }

    @Override
    public Optional<UsuarioWeb> findByEmail(String email) {
        try {
            List<UsuarioWeb> result = this.getFiltered(Filter.equalTo("email", email));
            if (result.isEmpty()) return Optional.empty();
            return Optional.of(result.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
