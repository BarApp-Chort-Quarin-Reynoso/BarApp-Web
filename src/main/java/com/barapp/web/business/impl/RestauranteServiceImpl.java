package com.barapp.web.business.impl;

import com.barapp.web.business.ImageContainer;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.ConfiguradorHorarioDao;
import com.barapp.web.data.dao.ImageDao;
import com.barapp.web.data.dao.RestauranteDao;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.Horario;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioWeb;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.google.cloud.firestore.Filter;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RestauranteServiceImpl extends BaseServiceImpl<Restaurante> implements RestauranteService {

    private final RestauranteDao restauranteDao;
    private final ConfiguradorHorarioDao configuradorHorarioDao;
    private final StorageClient storageClient;
    private final ImageDao imageDao;

    @Autowired
    public RestauranteServiceImpl(RestauranteDao restauranteDao, ConfiguradorHorarioDao configuradorHorarioDao, StorageClient storageClient, ImageDao imageDao) {
        this.restauranteDao = restauranteDao;
        this.configuradorHorarioDao = configuradorHorarioDao;
        this.storageClient = storageClient;
        this.imageDao = imageDao;
    }

    @Override
    public BaseDao<Restaurante, RestauranteEntity> getDao() {return restauranteDao;}

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
    public String saveConUsuario(Restaurante restaurante, UsuarioWeb usuario, ImageContainer logo, ImageContainer portada) {
        try {
            String logoUrl = imageDao.saveImage("images/logos/%s.%s", logo.getInputStream(), logo.getId(), logo.getContentType());
            String portadaUrl = imageDao.saveImage("images/fotos/%s.%s",portada.getInputStream(), portada.getId(), portada.getContentType());
            restaurante.setLogo(logoUrl);
            restaurante.setPortada(portadaUrl);

            return restauranteDao.saveConUsuario(restaurante, usuario);
        } catch (Exception e) {
            // TODO eliminar foto si falla
            throw new RuntimeException(e);
        }
    }

    @Override
    public String saveConFotos(Restaurante restaurante, ImageContainer logo, ImageContainer portada) {
        try {
            String logoUrl = imageDao.saveImage("images/logos/%s.%s", logo.getInputStream(), logo.getId(), logo.getContentType());
            String portadaUrl = imageDao.saveImage("images/fotos/%s.%s",portada.getInputStream(), portada.getId(), portada.getContentType());
            restaurante.setLogo(logoUrl);
            restaurante.setPortada(portadaUrl);

            return restauranteDao.save(restaurante, restaurante.getId());
        } catch (Exception e) {
            // TODO eliminar foto si falla
            throw new RuntimeException(e);
        }
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

    @Override
    public Map<LocalDate, List<Horario>> horariosEnMesDisponiblesSegunDiaHoraActual(String correoRestaurante, YearMonth mesAnio) {
        // La implementacion considera que los configuradores son devueltos segun su prioridad
        List<ConfiguradorHorario> configuradoresHorario
                = configuradorHorarioDao.getAllByCorreoRestaurante(correoRestaurante);

        Map<LocalDate, List<Horario>> horarios = new LinkedHashMap<>();

        LocalDate start = LocalDate.of(mesAnio.getYear(), mesAnio.getMonth(), 1);
        LocalDate end = start.plusMonths(1);

        if (start.isBefore(LocalDate.now())) {
            start = LocalDate.now();
        }

        for (LocalDate d : start.datesUntil(end).toList()) {
            for (ConfiguradorHorario ch : configuradoresHorario) {
                if (ch.isPermitido(d)) {
                    List<Horario> horariosGenerados = ch.generarHorarios();
                    if (d.isEqual(LocalDate.now())) {
                        horariosGenerados = horariosGenerados
                                .stream()
                                .filter(h -> h.getHorario().isAfter(LocalTime.now()))
                                .toList();
                    }
                    horarios.put(d, horariosGenerados);
                    break;
                }
            }
        }

        return horarios;
    }
}
