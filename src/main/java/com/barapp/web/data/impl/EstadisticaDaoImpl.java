package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.EstadisticaConverter;
import com.barapp.web.data.dao.EstadisticaDao;
import com.barapp.web.data.entities.EstadisticaEntity;
import com.barapp.web.model.Estadistica;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;

@Repository
public class EstadisticaDaoImpl extends BaseDaoImpl<Estadistica, EstadisticaEntity> implements EstadisticaDao {
    private final Firestore firestore;

    public EstadisticaDaoImpl(Firestore firestore) {
        super(EstadisticaEntity.class);
        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("estadisticas");
    }

    @Override
    public BaseConverter<Estadistica, EstadisticaEntity> getConverter() {
        return new EstadisticaConverter();
    }

    @Override
    public void sumarReservaConcretada(String idRestaurante) {
        try
        {
            DocumentReference docRef = getCollection().where(Filter.equalTo("idRestaurante", idRestaurante)).get().get().getDocuments().get(0).getReference();
            docRef.update("reservasConcretadas", FieldValue.increment(1)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
