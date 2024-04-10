package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.DetalleRestauranteConverter;
import com.barapp.web.data.dao.DetalleRestauranteDao;
import com.barapp.web.data.entities.DetalleRestauranteEntity;
import com.barapp.web.model.DetalleRestaurante;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

@Repository
public class DetalleRestauranteDaoImpl extends BaseDaoImpl<DetalleRestaurante, DetalleRestauranteEntity> implements DetalleRestauranteDao {

    private final Firestore firestore;

    public DetalleRestauranteDaoImpl(Firestore firestore) {
        super(DetalleRestauranteEntity.class);
        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("detallesRestaurantes");
    }

    @Override
    public BaseConverter<DetalleRestaurante, DetalleRestauranteEntity> getConverter() {
        return new DetalleRestauranteConverter();
    }
}
