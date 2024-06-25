package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.ConfiguracionConverter;
import com.barapp.web.data.dao.ConfiguracionDao;
import com.barapp.web.data.entities.ConfiguracionEntity;
import com.barapp.web.model.Configuracion;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

@Repository
public class ConfiguracionDaoImpl extends BaseDaoImpl<Configuracion, ConfiguracionEntity> implements ConfiguracionDao {

    private final Firestore firestore;

    public ConfiguracionDaoImpl(Firestore firestore) {
        super(ConfiguracionEntity.class);
        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("configuraciones");
    }

    @Override
    public BaseConverter<Configuracion, ConfiguracionEntity> getConverter() {
        return new ConfiguracionConverter();
    }

    @Override
    public Configuracion getRestaurantesConfig() {
        try {
            return this.get("cfg-restaurantes");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
