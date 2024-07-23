package com.barapp.web.data.impl;

import com.barapp.web.data.QueryParams;
import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.BaseModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<D> getAll() {
        List<D> result = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollection().get();
        try {
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                D object = getConverter().toDto(doc.toObject(clazz));
                object.setId(doc.getId());
                result.add(object);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }        
    }

    @Override
    public List<D> getAll(Set<Entry<String, String>> allParams) throws Exception {
        List<D> result = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollection().get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            D object = getConverter().toDto(doc.toObject(clazz));
            object.setId(doc.getId());
            result.add(object);
        }

        String order = null;
        String sortBy = null;
        int limit = Integer.MAX_VALUE;
        if (allParams != null && !allParams.isEmpty()) {
          for (Entry<String, String> entry : allParams) {
            String key = entry.getKey();
            String value = entry.getValue();
            if("continue".equals(key)){
              continue;
            }
            if ("order".equals(key)) {
              order = value;
              continue;
            }
            if ("sortBy".equals(key)) {
                sortBy = value;
                continue;
            }
            if ("limit".equals(key)) {
              limit = Integer.parseInt(value);
              continue;
            }
            result = result.stream().filter(obj -> {
                try {
                  Field field = obj.getClass().getDeclaredField(key);
                  field.setAccessible(true);
                  Object fieldValue = field.get(obj);
                  if (fieldValue instanceof String) {
                    return ((String) fieldValue).toLowerCase().contains(value.toLowerCase());
                  } else {
                      return fieldValue != null && fieldValue.toString().equals(value);
                  }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    return false;
                }
            }).collect(Collectors.toList());
          }
        }

        if (sortBy != null) {
          final String finalSortBy = sortBy;
          @SuppressWarnings({ "unchecked", "rawtypes" })
          Comparator<D> comparator = Comparator.comparing(obj -> {
            try {
                  Field field = obj.getClass().getDeclaredField(finalSortBy);
                  field.setAccessible(true);
                  Object fieldValue = field.get(obj);
                  if (!(fieldValue instanceof Comparable)) {
                      throw new RuntimeException("Field value is not Comparable");
                  }
                  return (Comparable) fieldValue;
              } catch (NoSuchFieldException | IllegalAccessException e) {
                  throw new RuntimeException(e);
              }
          });
          if ("desc".equals(order)) {
              comparator = comparator.reversed();
          }
          result.sort(comparator);
        }

        result = result.stream().limit(limit).collect(Collectors.toList());

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
