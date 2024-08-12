package com.barapp.web.data.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.dao.DetalleUsuarioDao;
import com.barapp.web.data.entities.DetalleUsuarioEntity;
import com.barapp.web.model.DetalleUsuario;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.barapp.web.data.converter.DetalleUsuarioConverter;

@Service
public class DetalleUsuarioDaoImpl extends BaseDaoImpl<DetalleUsuario, DetalleUsuarioEntity> implements DetalleUsuarioDao {

    private final Firestore firestore;

    public DetalleUsuarioDaoImpl(Firestore firestore) {
        super(DetalleUsuarioEntity.class);

        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("detallesUsuarios");
    }

    @Override
    public BaseConverter<DetalleUsuario, DetalleUsuarioEntity> getConverter() {
        return new DetalleUsuarioConverter();
    }

    @Override
    public DetalleUsuario updateRestaurantesFavoritos(String id, List<String> restaurantes) {
        try {
          DetalleUsuario detalleUsuario = get(id);
          detalleUsuario.setIdsRestaurantesFavoritos(restaurantes);
          save(detalleUsuario, id);
          return detalleUsuario;
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
    }

    @Override
    public DetalleUsuario updateBusquedasRecientes(String id, List<String> busquedasRecientes) {
        try {
          DetalleUsuario detalleUsuario = get(id);
          detalleUsuario.setBusquedasRecientes(busquedasRecientes);
          save(detalleUsuario, id);
          return detalleUsuario;
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
    }
}
