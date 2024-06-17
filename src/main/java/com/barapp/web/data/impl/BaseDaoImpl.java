package com.barapp.web.data.impl;

import com.barapp.web.data.QueryParams;
import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.BaseModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseDaoImpl<D extends BaseModel, E extends BaseEntity> implements BaseDao<D, E> {
    public final Class<E> clazz;

    protected BaseDaoImpl(Class<E> entityClass) {
        this.clazz = entityClass;
    }

    @Override
    public String save(D dto) throws Exception {
        return this.save(dto, null);
    }

    @Override
    public String save(D dto, String id) throws Exception {
        if (id == null || id.isEmpty()) {
            return getCollection().add(getConverter().toEntity(dto)).get().getId();
        }

        DocumentReference reference = getCollection().document(id);
        reference.set(getConverter().toEntity(dto)).get();
        return reference.getId();
    }

    @Override
    public void delete(String id) throws Exception {
        getCollection().document(id).delete().get();
    }

    @Override
    public D get(String id) throws Exception {
        DocumentReference ref = getCollection().document(id);
        ApiFuture<DocumentSnapshot> futureDoc = ref.get();
        DocumentSnapshot document = futureDoc.get();
        if (document.exists()) {
            D object = getConverter().toDto(document.toObject(clazz));
            object.setId(document.getId());
            return object;
        }
        return null;
    }

    @Override
    public List<D> getAll() throws Exception {
        List<D> result = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollection().get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            D object = getConverter().toDto(doc.toObject(clazz));
            object.setId(doc.getId());
            result.add(object);
        }
        return result;
    }

    @Override
    public List<D> getFiltered(Filter... filters) throws Exception {
        if (filters.length == 0) throw new IllegalArgumentException("Method requires at least one filter");

        Query query = null;
        for (Filter f : List.of(filters)) {
            if (query == null) query = getCollection().where(f);
            else query = query.where(f);
        }
        List<D> result = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = query.get().get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            D object = getConverter().toDto(doc.toObject(clazz));
            object.setId(doc.getId());
            result.add(object);
        }
        return result;
    }

    @Override
    public List<D> getByParams(QueryParams params) throws Exception {
        Query query = getCollection();
        for (Filter f : params.getFilters()) {
            query = query.where(f);
        }
        for (Map.Entry<String, Query.Direction> o : params.getOrders()) {
            query = query.orderBy(o.getKey(), o.getValue());
        }
        if (params.getLimit() != null) {
            query = query.limit(params.getLimit());
        }
        if (params.getOffset() != null) {
            query = query.offset(params.getOffset());
        }
        List<D> result = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = query.get().get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            D object = getConverter().toDto(doc.toObject(clazz));
            object.setId(doc.getId());
            result.add(object);
        }
        return result;
    }

    public abstract CollectionReference getCollection();

    public abstract BaseConverter<D, E> getConverter();
}
