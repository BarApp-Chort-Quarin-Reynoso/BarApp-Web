package com.barapp.web.data.impl;

import com.barapp.web.data.QueryParams;
import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.ConfiguradorHorarioConverter;
import com.barapp.web.data.dao.ConfiguradorHorarioDao;
import com.barapp.web.data.entities.ConfiguradorHorarioEntity;
import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioNoLaboral;
import com.barapp.web.model.enums.TipoConfigurador;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfiguradorHorarioDaoImpl extends BaseDaoImpl<ConfiguradorHorario, ConfiguradorHorarioEntity> implements ConfiguradorHorarioDao {

    private final Firestore firestore;

    protected ConfiguradorHorarioDaoImpl(Firestore firestore) {
        super(ConfiguradorHorarioEntity.class);
        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("configuradoresHorario");
    }

    @Override
    public BaseConverter<ConfiguradorHorario, ConfiguradorHorarioEntity> getConverter() {
        return new ConfiguradorHorarioConverter();
    }

    @Override
    public List<ConfiguradorHorario> getAllByCorreoRestaurante(String correo) {
        try {
            QueryParams queryParams = new QueryParams();
            queryParams.addFilter(Filter.equalTo("correoRestaurante", correo));
            queryParams.addOrder("tipo", Query.Direction.ASCENDING);

            return this.getByParams(queryParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ConfiguradorHorarioNoLaboral> getAllNoLaboralByCorreoRestaurante(String correo) {
        try {
            QueryParams queryParams = new QueryParams();
            queryParams.addFilter(Filter.equalTo("correoRestaurante", correo));
            queryParams.addFilter(Filter.equalTo("tipo", TipoConfigurador.NO_LABORAL.getOrden()));

            return this.getByParams(queryParams).stream().map(ch -> (ConfiguradorHorarioNoLaboral) ch).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
