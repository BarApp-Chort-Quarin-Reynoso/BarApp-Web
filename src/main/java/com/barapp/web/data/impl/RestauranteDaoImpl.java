package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.RestauranteConverter;
import com.barapp.web.data.dao.EstadisticaDao;
import com.barapp.web.data.dao.HorarioPorRestauranteDao;
import com.barapp.web.data.dao.RestauranteDao;
import com.barapp.web.data.dao.UsuarioWebDao;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.TipoComida;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Map;

@Service
public class RestauranteDaoImpl extends BaseDaoImpl<Restaurante, RestauranteEntity> implements RestauranteDao {

    private final Firestore firestore;
    private final UsuarioWebDao usuarioWebDao;
    private final HorarioPorRestauranteDao horarioPorRestauranteDao;
    private final EstadisticaDao estadisticaDao;

    public RestauranteDaoImpl(Firestore firestore, UsuarioWebDao usuarioWebDao, HorarioPorRestauranteDao horarioPorRestauranteDao, EstadisticaDao estadisticaDao) {
        super(RestauranteEntity.class);

        this.firestore = firestore;
        this.usuarioWebDao = usuarioWebDao;
        this.horarioPorRestauranteDao = horarioPorRestauranteDao;
        this.estadisticaDao = estadisticaDao;
    }


    @SuppressWarnings("unchecked")
    @Override
    public String registrarRestaurante(Restaurante restaurante, UsuarioWeb usuarioWeb, HorarioPorRestaurante horarios) throws Exception {
        Estadistica nuevaEstadistica = Estadistica.builder()
                .idRestaurante(restaurante.getId())
                .correo(restaurante.getCorreo())
                .reservasConcretadas(0)
                .diasActivo(0)
                .clientesAtendidos(0)
                .porcentajeOcupacionxDiaSemana(Map.ofEntries(
                        Arrays.stream(DayOfWeek.values()).map(day -> Map.entry(day, 0.0)).toArray(Map.Entry[]::new)
                ))
                .porcentajeOcupacionxTipoComida(Map.ofEntries(
                        Arrays.stream(TipoComida.values()).map(day -> Map.entry(day, 0.0)).toArray(Map.Entry[]::new)
                ))
                .build();

        WriteBatch batch = firestore.batch();
        batch.set(getCollection().document(restaurante.getId()), getConverter().toEntity(restaurante));
        batch.set(
                usuarioWebDao.getCollection().document(usuarioWeb.getId()),
                usuarioWebDao.getConverter().toEntity(usuarioWeb)
        );
        batch.set(
                horarioPorRestauranteDao.getCollection().document(horarios.getId()),
                horarioPorRestauranteDao.getConverter().toEntity(horarios)
        );
        batch.set(
                estadisticaDao.getCollection().document(nuevaEstadistica.getId()),
                estadisticaDao.getConverter().toEntity(nuevaEstadistica)
        );
        batch.commit().get();
        return restaurante.getId();
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("restaurantes");
    }

    @Override
    public BaseConverter<Restaurante, RestauranteEntity> getConverter() {
        return new RestauranteConverter();
    }

    @Override
    public void actualizarPorNuevaOpinion(Restaurante restaurante, Opinion opinion, Integer nuevaCantidadOpiniones, Double nuevaPuntuacion) throws Exception {
        restaurante.setCantidadOpiniones(nuevaCantidadOpiniones);
        restaurante.setPuntuacion(nuevaPuntuacion);
    }
}
