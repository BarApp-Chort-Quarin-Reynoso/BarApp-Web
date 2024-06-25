package com.barapp.web.data.impl;

import com.barapp.web.data.QueryParams;
import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.OpinionConverter;
import com.barapp.web.data.dao.OpinionDao;
import com.barapp.web.data.entities.OpinionEntity;
import com.barapp.web.model.Opinion;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OpinionDaoImpl extends BaseDaoImpl<Opinion, OpinionEntity> implements OpinionDao {
    private final Firestore firestore;

    public OpinionDaoImpl(Firestore firestore) {
        super(OpinionEntity.class);

        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("opiniones");
    }

    @Override
    public BaseConverter<Opinion, OpinionEntity> getConverter() {
        return new OpinionConverter();
    }

    @Override
    public List<Opinion> getOpinionesByRestaurante(String idRestaurante) {
        try {
            QueryParams queryParams = new QueryParams();
            queryParams.addFilter(Filter.equalTo("idRestaurante", idRestaurante));
            queryParams.addOrder("fecha", Query.Direction.ASCENDING);

            return this.getByParams(queryParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Opinion> getAllOpinionesRecientesByRestaurante(String idRestaurante) {
        try {
            QueryParams queryParams = new QueryParams();
            queryParams.addFilter(Filter.equalTo("idRestaurante", idRestaurante));
            queryParams.addOrder("fecha", Query.Direction.ASCENDING);
            queryParams.setLimit(3);

            return this.getByParams(queryParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
