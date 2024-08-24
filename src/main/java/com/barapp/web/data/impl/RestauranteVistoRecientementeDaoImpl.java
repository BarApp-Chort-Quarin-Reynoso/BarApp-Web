package com.barapp.web.data.impl;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.RestauranteUsuarioConverter;
import com.barapp.web.data.dao.RestauranteVistoRecientementeDao;
import com.barapp.web.data.entities.RestauranteUsuarioEntity;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Filter;

@Service
public class RestauranteVistoRecientementeDaoImpl extends BaseDaoImpl<RestauranteUsuario, RestauranteUsuarioEntity> implements RestauranteVistoRecientementeDao {

    private final Firestore firestore;

    public RestauranteVistoRecientementeDaoImpl(Firestore firestore) {
        super(RestauranteUsuarioEntity.class);

        this.firestore = firestore;
    }

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("restaurantesVistosRecientemente");
    }

    @Override
    public BaseConverter<RestauranteUsuario, RestauranteUsuarioEntity> getConverter() {
        return new RestauranteUsuarioConverter();
    }

    @Override
    public List<RestauranteUsuario> getByUserId(String userId) {
        try {
            List<RestauranteUsuario> vistosRecientemente = this
                .getFiltered(Filter
                    .and(Filter.equalTo("idUsuario", userId), Filter
                        .or(Filter.equalTo("estado", EstadoRestaurante.HABILITADO), Filter
                            .equalTo("estado", EstadoRestaurante.PAUSADO))));

          Pattern pattern = Pattern.compile("Timestamp\\(seconds=(\\d+), nanoseconds=(\\d+)\\)");

          return vistosRecientemente.stream()
                  .sorted((r1, r2) -> {
                      try {
                          Matcher matcher1 = pattern.matcher(r1.getFechaGuardado());
                          Matcher matcher2 = pattern.matcher(r2.getFechaGuardado());
  
                          if (matcher1.find() && matcher2.find()) {
                              long seconds1 = Long.parseLong(matcher1.group(1));
                              long nanoseconds1 = Long.parseLong(matcher1.group(2));
                              Date date1 = new Date(seconds1 * 1000 + nanoseconds1 / 1000000);
  
                              long seconds2 = Long.parseLong(matcher2.group(1));
                              long nanoseconds2 = Long.parseLong(matcher2.group(2));
                              Date date2 = new Date(seconds2 * 1000 + nanoseconds2 / 1000000);
  
                              return date2.compareTo(date1);
                          }
                      } catch (Exception e) {
                          e.printStackTrace();
                          return 0;
                      }
                      return 0;
                  })
                  .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
  
}
