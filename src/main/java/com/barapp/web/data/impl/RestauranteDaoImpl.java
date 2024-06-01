package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.RestauranteConverter;
import com.barapp.web.data.dao.RestauranteDao;
import com.barapp.web.data.dao.UsuarioWebDao;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioWeb;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import org.springframework.stereotype.Service;

@Service
public class RestauranteDaoImpl extends BaseDaoImpl<Restaurante, RestauranteEntity> implements RestauranteDao {

    private final Firestore firestore;
    private final UsuarioWebDao usuarioWebDao;

    public RestauranteDaoImpl(Firestore firestore, UsuarioWebDao usuarioWebDao) {
        super(RestauranteEntity.class);

        this.firestore = firestore;
        this.usuarioWebDao = usuarioWebDao;
    }

    @Override
    public String saveConUsuario(Restaurante restaurante, UsuarioWeb usuarioWeb) throws Exception {
        WriteBatch batch = firestore.batch();
        batch.set(getCollection().document(restaurante.getId()), getConverter().toEntity(restaurante));
        batch.set(
                usuarioWebDao.getCollection().document(usuarioWeb.getId()),
                usuarioWebDao.getConverter().toEntity(usuarioWeb)
        );
        batch.commit().get();
        return restaurante.getId();
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("restaurantes");
    }

    @Override
    public BaseConverter<Restaurante, RestauranteEntity> getConverter() {
        return new RestauranteConverter();
    }

}
