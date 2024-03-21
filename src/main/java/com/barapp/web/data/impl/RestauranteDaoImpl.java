package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.RestauranteConverter;
import com.barapp.web.data.dao.RestauranteDao;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.Restaurante;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Service;

@Service
public class RestauranteDaoImpl extends BaseDaoImpl<Restaurante, RestauranteEntity> implements RestauranteDao {

    private final Firestore firestore;

    public RestauranteDaoImpl(Firestore firestore) {
        super(RestauranteEntity.class);

        this.firestore = firestore;
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
