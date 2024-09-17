package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.DetalleRestauranteConverter;
import com.barapp.web.data.dao.DetalleRestauranteDao;
import com.barapp.web.data.entities.DetalleRestauranteEntity;
import com.barapp.web.model.CalificacionPromedio;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.Opinion;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DetalleRestauranteDaoImpl extends BaseDaoImpl<DetalleRestaurante, DetalleRestauranteEntity> implements DetalleRestauranteDao {

    private final Firestore firestore;

    public DetalleRestauranteDaoImpl(Firestore firestore) {
        super(DetalleRestauranteEntity.class);
        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("detallesRestaurantes");
    }

    @Override
    public BaseConverter<DetalleRestaurante, DetalleRestauranteEntity> getConverter() {
        return new DetalleRestauranteConverter();
    }

    @Override
    public void actualizarCaracteristicas(String idDetalleRestaurante, Map<String, CalificacionPromedio> caracteristicas) {
        DocumentReference docRef = getCollection().document(idDetalleRestaurante);
        try {
            docRef.update("caracteristicas", caracteristicas).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizarPorNuevaOpinion(DetalleRestaurante detalleRestaurante, Opinion opinion) {
        for (Map.Entry<String, Integer> caracteristicaValoracion : opinion.getCaracteristicas().entrySet()) {
            String caracteristica = caracteristicaValoracion.getKey();
            Integer valoracion = caracteristicaValoracion.getValue();

            CalificacionPromedio cantidadOpinionesYValoracion = detalleRestaurante
                    .getCaracteristicas()
                    .get(caracteristica);
            Double nuevaValoracionCaracteristica = (cantidadOpinionesYValoracion
                    .getPuntuacion()
                    * cantidadOpinionesYValoracion
                            .getCantidadOpiniones()
                    + valoracion)
                    / (cantidadOpinionesYValoracion
                            .getCantidadOpiniones() + 1);
            nuevaValoracionCaracteristica = Math.round(nuevaValoracionCaracteristica * 10.0) / 10.0;
            cantidadOpinionesYValoracion.setPuntuacion(nuevaValoracionCaracteristica);
            cantidadOpinionesYValoracion
                    .setCantidadOpiniones(cantidadOpinionesYValoracion.getCantidadOpiniones() + 1);

            detalleRestaurante.getCaracteristicas().put(caracteristica, cantidadOpinionesYValoracion);
        }
    }
}
