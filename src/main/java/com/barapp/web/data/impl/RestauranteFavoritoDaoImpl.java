package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.RestauranteUsuarioConverter;
import com.barapp.web.data.dao.RestauranteFavoritoDao;
import com.barapp.web.data.entities.RestauranteUsuarioEntity;
import com.barapp.web.model.Opinion;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteFavoritoDaoImpl extends BaseDaoImpl<RestauranteUsuario, RestauranteUsuarioEntity> implements RestauranteFavoritoDao {

    private final Firestore firestore;

    public RestauranteFavoritoDaoImpl(Firestore firestore) {
        super(RestauranteUsuarioEntity.class);

        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("restaurantesFavoritos");
    }

    @Override
    public BaseConverter<RestauranteUsuario, RestauranteUsuarioEntity> getConverter() {
        return new RestauranteUsuarioConverter();
    }

    @Override
    public List<RestauranteUsuario> getByUserId(String userId) {
        try {
            List<RestauranteUsuario> userFavoriteRestaurants = this.getFiltered(Filter
                    .and(Filter.equalTo("idUsuario", userId), Filter
                            .or(Filter.equalTo("estado", EstadoRestaurante.HABILITADO), Filter
                                    .equalTo("estado", EstadoRestaurante.PAUSADO))));
            userFavoriteRestaurants.sort((a, b) -> b.getFechaGuardado().compareTo(a.getFechaGuardado()));
            return userFavoriteRestaurants;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void actualizarYGuardarPorNuevaOpinion(Restaurante restaurante, Opinion opinion, Integer nuevaCantidadOpiniones, Double nuevaPuntuacion) throws Exception {
        this.getFiltered(Filter.equalTo("idRestaurante", restaurante.getId()))
                .forEach(restauranteUsuario -> {
                    restauranteUsuario.setCantidadOpiniones(nuevaCantidadOpiniones);
                    restauranteUsuario.setPuntuacion(nuevaPuntuacion);
                    try {
                        this.save(restauranteUsuario, restauranteUsuario.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
