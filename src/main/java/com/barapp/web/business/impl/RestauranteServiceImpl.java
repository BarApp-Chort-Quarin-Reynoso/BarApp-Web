package com.barapp.web.business.impl;

import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.RestauranteDao;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.EstadoRestaurante;
import com.barapp.web.model.Restaurante;
import com.google.cloud.firestore.Filter;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RestauranteServiceImpl extends BaseServiceImpl<Restaurante> implements RestauranteService {

    private final RestauranteDao restauranteDao;
    private final StorageClient storageClient;

    @Autowired
    public RestauranteServiceImpl(RestauranteDao restauranteDao, StorageClient storageClient) {
        this.restauranteDao = restauranteDao;
        this.storageClient = storageClient;
    }

    @Override
    public BaseDao<Restaurante, RestauranteEntity> getDao() { return restauranteDao; }

    @Override
    public String saveLogo(InputStream inputStream, String id, String contentType) {
        return this.saveImage("images/logos/%s.%s", inputStream, id, contentType);
    }

    @Override
    public String savePortada(InputStream inputStream, String id, String contentType) {
        return this.saveImage("images/fotos/%s.%s", inputStream, id, contentType);
    }

    private String saveImage(String dest, InputStream inputStream, String id, String contentType) {
        String blobString = String.format(dest, id, contentType.substring(contentType.indexOf("/") + 1));
        Blob blob = storageClient
            .bucket()
                .create(blobString, inputStream, contentType, Bucket.BlobWriteOption.userProject("barapp-b1bc0"));
        URL signedUrl = blob.signUrl(32850, TimeUnit.DAYS);

        return signedUrl.toString();
    }

    @Override
    @Transactional
    public void rechazarRestaurante(Restaurante restaurante) {
        if (!restaurante.getEstado().equals(EstadoRestaurante.ESPERANDO_HABILITACION))
            throw new RuntimeException("El restaurante %s ya ha pasado la etapa de verificación"
                .formatted(restaurante.getNombre()));

        try {
            restaurante.setEstado(EstadoRestaurante.RECHAZADO);
            restauranteDao.save(restaurante, restaurante.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aceptarRestaurante(Restaurante restaurante) {
        if (!restaurante.getEstado().equals(EstadoRestaurante.ESPERANDO_HABILITACION))
            throw new RuntimeException("El restaurante %s ya ha pasado la etapa de verificación"
                .formatted(restaurante.getNombre()));

        try {
            restaurante.setEstado(EstadoRestaurante.HABILITADO);
            restauranteDao.save(restaurante, restaurante.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Restaurante> getByCorreo(String correo) {
        try {
            List<Restaurante> restaurantes = restauranteDao.getFiltered(Filter.equalTo("correo", correo));
            if (restaurantes.isEmpty()) return Optional.empty();
            
            return Optional.of(restaurantes.get(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
