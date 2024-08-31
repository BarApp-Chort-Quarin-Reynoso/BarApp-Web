package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.HorarioPorRestauranteConverter;
import com.barapp.web.data.dao.HorarioPorRestauranteDao;
import com.barapp.web.data.entities.HorarioPorRestauranteEntity;
import com.barapp.web.model.HorarioPorRestaurante;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class HorarioPorRestauranteDaoImpl extends BaseDaoImpl<HorarioPorRestaurante, HorarioPorRestauranteEntity> implements HorarioPorRestauranteDao {

    private final Firestore firestore;

    public HorarioPorRestauranteDaoImpl(Firestore firestore) {
        super(HorarioPorRestauranteEntity.class);
        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("horariosPorRestaurante");
    }

    @Override
    public BaseConverter<HorarioPorRestaurante, HorarioPorRestauranteEntity> getConverter() {
        return new HorarioPorRestauranteConverter();
    }

    @Override
    public Optional<HorarioPorRestaurante> getByCorreoRestaurante(String correo) {
        try {
            return getFiltered(Filter.equalTo("correo", correo))
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
