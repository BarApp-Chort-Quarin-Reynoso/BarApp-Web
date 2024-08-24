package com.barapp.web.data.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.RestauranteUsuarioConverter;
import com.barapp.web.data.dao.RestauranteFavoritoDao;
import com.barapp.web.data.entities.RestauranteUsuarioEntity;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Filter;

@Service
public class RestauranteFavoritoDaoImpl extends BaseDaoImpl<RestauranteUsuario, RestauranteUsuarioEntity> implements RestauranteFavoritoDao {

    private final Firestore firestore;

    public RestauranteFavoritoDaoImpl(Firestore firestore) {
        super(RestauranteUsuarioEntity.class);

        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("restaurantesFavoritos");
    }

    @Override
    public BaseConverter<RestauranteUsuario, RestauranteUsuarioEntity> getConverter() {
        return new RestauranteUsuarioConverter();
    }

    @Override
    public List<RestauranteUsuario> getByUserId(String userId) {
        try {
            return this
                .getFiltered(Filter
                    .and(Filter.equalTo("idUsuario", userId), Filter
                        .or(Filter.equalTo("estado", EstadoRestaurante.HABILITADO), Filter
                            .equalTo("estado", EstadoRestaurante.PAUSADO))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
  
}
