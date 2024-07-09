package com.barapp.web.business.impl;

import com.barapp.web.business.ImageContainer;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.data.QueryParams;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.ConfiguradorHorarioDao;
import com.barapp.web.data.dao.ImageDao;
import com.barapp.web.data.dao.DetalleRestauranteDao;
import com.barapp.web.data.dao.RestauranteDao;
import com.barapp.web.data.dao.RestauranteVistoRecientementeDao;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.Horario;
import com.barapp.web.model.ConfiguradorHorarioSemanal;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.UsuarioWeb;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.utils.Tuple;
import com.google.cloud.firestore.Filter;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RestauranteServiceImpl extends BaseServiceImpl<Restaurante> implements RestauranteService {

    private final RestauranteDao restauranteDao;
    private final RestauranteVistoRecientementeDao restauranteVistoRecientementeDao;
    private final ConfiguradorHorarioDao configuradorHorarioDao;
    private final DetalleRestauranteDao detalleRestauranteDao;
    private final StorageClient storageClient;
    private final ImageDao imageDao;

    public RestauranteServiceImpl(RestauranteDao restauranteDao, RestauranteVistoRecientementeDao restauranteVistoRecientementeDao, ConfiguradorHorarioDao configuradorHorarioDao, DetalleRestauranteDao detalleRestauranteDao, StorageClient storageClient, ImageDao imageDao) {
        this.restauranteDao = restauranteDao;
      this.restauranteVistoRecientementeDao = restauranteVistoRecientementeDao;
        this.configuradorHorarioDao = configuradorHorarioDao;
        this.detalleRestauranteDao = detalleRestauranteDao;
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
            String logoUrl = imageDao.saveImage(
                    "images/logos/%s.%s", logo.getInputStream(), logo.getId(), logo.getContentType());
            String portadaUrl = imageDao.saveImage(
                    "images/fotos/%s.%s", portada.getInputStream(), portada.getId(), portada.getContentType());
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
            String logoUrl = imageDao.saveImage(
                    "images/logos/%s.%s", logo.getInputStream(), logo.getId(), logo.getContentType());
            String portadaUrl = imageDao.saveImage(
                    "images/fotos/%s.%s", portada.getInputStream(), portada.getId(), portada.getContentType());
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

    @Override
    public Map<LocalDate, Tuple<List<Horario>, ConfiguradorHorario>> horariosEnMesDisponiblesSegunMesAnioConConfiguradorCoincidente(String correoRestaurante, YearMonth mesAnio) {
        // La implementacion considera que los configuradores son devueltos segun su prioridad
        List<ConfiguradorHorario> configuradoresHorario
                = configuradorHorarioDao.getAllByCorreoRestaurante(correoRestaurante);

        Map<LocalDate, Tuple<List<Horario>, ConfiguradorHorario>> horarios = new LinkedHashMap<>();

        LocalDate start = LocalDate.of(mesAnio.getYear(), mesAnio.getMonth(), 1);
        LocalDate end = start.plusMonths(1);

        if (start.isBefore(LocalDate.now())) {
            start = LocalDate.now();
        }

        for (LocalDate d : start.datesUntil(end).toList()) {
            for (ConfiguradorHorario ch : configuradoresHorario) {
                if (ch.isPermitido(d)) {
                    List<Horario> horariosGenerados = ch.generarHorarios();
                    horarios.put(d, new Tuple<>(horariosGenerados, ch));
                    break;
                }
            }
        }

        return horarios;
    }

    @Override
    public Optional<DetalleRestaurante> getRestaurantDetail(String id) {
        try {
            return Optional.ofNullable(detalleRestauranteDao.get(id));
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RestauranteUsuario addVistoRecientemente(String idRestaurante, RestauranteUsuario restaurante) {
      try {
        if (restauranteDao.get(restaurante.getIdRestaurante()) == null) {
          throw new IllegalStateException("El restaurante con ID " + restaurante.getIdRestaurante() + " no existe.");
        }

        QueryParams queryParams = new QueryParams();
        queryParams.addFilter(Filter.equalTo("idRestaurante", restaurante.getIdRestaurante()));
        queryParams.addFilter(Filter.equalTo("idUsuario", restaurante.getIdUsuario()));

        if (!restauranteVistoRecientementeDao.getByParams(queryParams).isEmpty()) {
          throw new IllegalStateException("El restaurante con ID " + restaurante.getIdRestaurante() + " ya existe para el usuario con ID " + restaurante.getIdUsuario() + ".");
        }

        restauranteVistoRecientementeDao.save(restaurante, idRestaurante);

        List<RestauranteUsuario> restaurantes = restauranteVistoRecientementeDao.getFiltered(Filter.equalTo("idUsuario", restaurante.getIdUsuario()));
        // Ordena segun fechaGuardado para eliminar el mas antiguo (si hay mas de 5)
        restaurantes.sort(Comparator.comparing(r -> {
          try {
            String fechaGuardado = ((RestauranteUsuario) r).getFechaGuardado();
            long seconds = Long.parseLong(fechaGuardado.substring(fechaGuardado.indexOf('=') + 1, fechaGuardado.indexOf(',')));
            int nanos = Integer.parseInt(fechaGuardado.substring(fechaGuardado.lastIndexOf('=') + 1, fechaGuardado.indexOf(')')));

            return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds, nanos), ZoneId.systemDefault());
          } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            System.err.println("Error al parsear fechaGuardado: " + e.getMessage());
            return LocalDateTime.MIN;
          }
        }));
        while (restaurantes.size() > 5) {
          restauranteVistoRecientementeDao.delete(restaurantes.get(0).getId());
          restaurantes.remove(0);
        }
          return restaurante;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
