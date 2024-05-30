package com.barapp.web.data.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.RestauranteConverter;
import com.barapp.web.data.dao.RestauranteFavoritoDao;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.Restaurante;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Filter;

@Service
public class RestauranteFavoritoDaoImpl extends BaseDaoImpl<Restaurante, RestauranteEntity> implements RestauranteFavoritoDao {

    private final Firestore firestore;

    public RestauranteFavoritoDaoImpl(Firestore firestore) {
        super(RestauranteEntity.class);

        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("restaurantesFavoritos");
    }

    @Override
    public BaseConverter<Restaurante, RestauranteEntity> getConverter() {
        return new RestauranteConverter();
    }

    @Override
    public List<Restaurante> getByUserId(String userId) {
        try {
            return this.getFiltered(Filter.equalTo("idUsuario", userId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
  
}
