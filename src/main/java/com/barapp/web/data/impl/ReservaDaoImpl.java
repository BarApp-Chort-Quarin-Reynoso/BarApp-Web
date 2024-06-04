package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.ReservaConverter;
import com.barapp.web.data.dao.ReservaDao;
import com.barapp.web.data.entities.ReservaEntity;
import com.barapp.web.model.Reserva;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Service;

@Service
public class ReservaDaoImpl extends BaseDaoImpl<Reserva, ReservaEntity> implements ReservaDao {
    private final Firestore firestore;

    public ReservaDaoImpl(Firestore firestore) {
        super(ReservaEntity.class);

        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("reservas");
    }

    @Override
    public BaseConverter<Reserva, ReservaEntity> getConverter() {
        return new ReservaConverter();
    }
}

