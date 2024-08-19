package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.RestauranteConverter;
import com.barapp.web.data.dao.HorarioPorRestauranteDao;
import com.barapp.web.data.dao.RestauranteDao;
import com.barapp.web.data.dao.UsuarioWebDao;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.HorarioPorRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioWeb;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import com.google.type.LatLng;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteDaoImpl extends BaseDaoImpl<Restaurante, RestauranteEntity> implements RestauranteDao {

    private final Firestore firestore;
    private final UsuarioWebDao usuarioWebDao;
    private final HorarioPorRestauranteDao horarioPorRestauranteDao;

    public RestauranteDaoImpl(Firestore firestore, UsuarioWebDao usuarioWebDao, HorarioPorRestauranteDao horarioPorRestauranteDao) {
        super(RestauranteEntity.class);

        this.firestore = firestore;
        this.usuarioWebDao = usuarioWebDao;
        this.horarioPorRestauranteDao = horarioPorRestauranteDao;
    }

    @Override
    public String registrarRestaurante(Restaurante restaurante, UsuarioWeb usuarioWeb, HorarioPorRestaurante horarios) throws Exception {
        WriteBatch batch = firestore.batch();
        batch.set(getCollection().document(restaurante.getId()), getConverter().toEntity(restaurante));
        batch.set(
                usuarioWebDao.getCollection().document(usuarioWeb.getId()),
                usuarioWebDao.getConverter().toEntity(usuarioWeb)
        );
        batch.set(
                horarioPorRestauranteDao.getCollection().document(horarios.getId()),
                horarioPorRestauranteDao.getConverter().toEntity(horarios)
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
