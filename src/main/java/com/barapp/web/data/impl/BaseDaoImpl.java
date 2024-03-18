package com.barapp.web.data.impl;

import java.util.ArrayList;
import java.util.List;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.BaseDto;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public abstract class BaseDaoImpl<D extends BaseDto, E extends BaseEntity> implements BaseDao<D, E> {
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
	if (id == null || id.length() == 0) {
	    return getCollection().add(getConverter().toEntity(dto)).get().getId();
	}

	DocumentReference reference = getCollection().document(id);
	reference.set(getConverter().toEntity(dto));
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

    public abstract CollectionReference getCollection();
    
    public abstract BaseConverter<D, E> getConverter();
}
