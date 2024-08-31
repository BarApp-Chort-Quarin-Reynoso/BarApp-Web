package com.barapp.web.business.impl;

import com.barapp.web.business.ImageContainer;
import com.barapp.web.business.service.HorarioPorRestauranteService;
import com.barapp.web.business.service.ReservaService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.data.QueryParams;
import com.barapp.web.data.dao.*;
import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.utils.Tuple;
import com.google.cloud.firestore.Filter;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.google.type.LatLng;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URL;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RestauranteServiceImpl extends BaseServiceImpl<Restaurante> implements RestauranteService {

    private final RestauranteDao restauranteDao;
    private final RestauranteFavoritoDao restauranteFavoritoDao;
    private final RestauranteVistoRecientementeDao restauranteVistoRecientementeDao;
    private final ConfiguradorHorarioDao configuradorHorarioDao;
    private final DetalleRestauranteDao detalleRestauranteDao;
    private final DetalleUsuarioDao detalleUsuarioDao;
    private final HorarioPorRestauranteService horarioPorRestauranteService;
    private final ReservaService reservaService;
    private final StorageClient storageClient;
    private final ImageDao imageDao;

    public RestauranteServiceImpl(RestauranteDao restauranteDao, RestauranteFavoritoDao restauranteFavoritoDao, RestauranteVistoRecientementeDao restauranteVistoRecientementeDao, ConfiguradorHorarioDao configuradorHorarioDao, DetalleRestauranteDao detalleRestauranteDao, DetalleUsuarioDao detalleUsuarioDao, HorarioPorRestauranteService horarioPorRestauranteService, ReservaService reservaService, StorageClient storageClient, ImageDao imageDao) {
        this.restauranteDao = restauranteDao;
        this.restauranteFavoritoDao = restauranteFavoritoDao;
        this.restauranteVistoRecientementeDao = restauranteVistoRecientementeDao;
        this.configuradorHorarioDao = configuradorHorarioDao;
        this.detalleRestauranteDao = detalleRestauranteDao;
        this.detalleUsuarioDao = detalleUsuarioDao;
        this.horarioPorRestauranteService = horarioPorRestauranteService;
        this.reservaService = reservaService;
        this.storageClient = storageClient;
        this.imageDao = imageDao;
    }

    @Override
    public BaseDao<Restaurante, RestauranteEntity> getDao() {return restauranteDao;}

    @Override
    public List<Restaurante> getAvailableOrPausedRestaurants() {
        try {
            return restauranteDao
                    .getFiltered(Filter
                            .or(
                                    Filter.equalTo("estado", EstadoRestaurante.HABILITADO),
                                    Filter.equalTo("estado", EstadoRestaurante.PAUSADO)
                            ));
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

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
    public String registrarRestaurante(Restaurante restaurante, UsuarioWeb usuario, ImageContainer logo, ImageContainer portada) {
        try {
            String logoUrl = imageDao
                    .saveImage(
                            "images/logos/%s.%s", logo.getInputStream(), logo.getId(), logo.getContentType());
            String portadaUrl = imageDao
                    .saveImage(
                            "images/fotos/%s.%s", portada.getInputStream(), portada.getId(), portada.getContentType());
            restaurante.setLogo(logoUrl);
            restaurante.setPortada(portadaUrl);
            HorarioPorRestaurante horarioPorRestaurante = HorarioPorRestaurante
                    .builder()
                    .idRestaurante(restaurante.getId())
                    .correo(restaurante.getCorreo())
                    .build();

            return restauranteDao.registrarRestaurante(restaurante, usuario, horarioPorRestaurante);
        } catch (Exception e) {
            // TODO eliminar foto si falla
            throw new RuntimeException(e);
        }
    }

    @Override
    public String saveConFotos(Restaurante restaurante, ImageContainer logo, ImageContainer portada) {
        try {
            String logoUrl = imageDao
                    .saveImage(
                            "images/logos/%s.%s", logo.getInputStream(), logo.getId(), logo.getContentType());
            String portadaUrl = imageDao
                    .saveImage(
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
    public Map<LocalDate, Map<String, HorarioConCapacidadDisponible>> horariosEnMesDisponiblesSegunDiaHoraActual(String correoRestaurante, YearMonth mesAnio) {
        Optional<HorarioPorRestaurante> horarioPorRestaurante = horarioPorRestauranteService
                .getByCorreoRestaurante(correoRestaurante);

        if (horarioPorRestaurante.isEmpty()) {
            return new LinkedHashMap<>();
        }

        Map<LocalDate, List<Reserva>> reservasPorDia = reservaService
                .getReservasPendientesPorMes(horarioPorRestaurante.get().getIdRestaurante(), mesAnio);

        Collection<ConfiguradorHorario> configuradoresHorario = horarioPorRestaurante
                .get()
                .getConfiguradores()
                .values();

        Map<LocalDate, Map<String, HorarioConCapacidadDisponible>> horarios = new LinkedHashMap<>();

        LocalDate start = LocalDate.of(mesAnio.getYear(), mesAnio.getMonth(), 1);
        LocalDate end = start.plusMonths(1);

        if (start.isBefore(LocalDate.now())) {
            start = LocalDate.now();
        }

        for (LocalDate d : start.datesUntil(end).toList()) {
            for (ConfiguradorHorario ch : configuradoresHorario) {
                if (ch.isPermitido(d)) {
                    List<Horario> horariosGenerados = ch.generarHorarios();

                    // Busca la capacidad por comida. Si no tiene una especifica, se usa la general
                    Map<TipoComida, Set<Mesa>> capacidadPorComida = ch.getCapacidadPorComida();
                    for (TipoComida tipoComida : TipoComida.values()) {
                        if (capacidadPorComida.get(tipoComida).isEmpty() && ch
                                .tieneHorariosParaTipoComida(
                                        tipoComida)) {
                            capacidadPorComida
                                    .put(tipoComida, horarioPorRestaurante
                                            .get()
                                            .getMesas()
                                            .stream()
                                            .map(Mesa::new)
                                            .collect(Collectors.toCollection(LinkedHashSet::new)));
                        }
                    }
                    List<Reserva> reservas = reservasPorDia.getOrDefault(d, List.of());

                    // Se calcula la capacidad disponible
                    for (Reserva r : reservas) {
                        TipoComida tipoComida = r.getHorario().getTipoComida();
                        Optional<Mesa> mesaOcupadaOpt = capacidadPorComida
                                .get(tipoComida)
                                .stream()
                                .sorted(Comparator.comparing(Mesa::getCantidadDePersonasPorMesa))
                                .filter(m -> m.getCantidadDePersonasPorMesa() >= r.getCantidadPersonas())
                                .findFirst();

                        if (mesaOcupadaOpt.isEmpty() || mesaOcupadaOpt.get().getCantidadMesas() == 0) {
                            throw new IllegalStateException(
                                    "Las reservas son inconsistentes con las mesas disponibles");
                        }

                        Mesa mesa = mesaOcupadaOpt.get();
                        mesa.setCantidadMesas(mesa.getCantidadMesas() - 1);
                        if (mesa.getCantidadMesas() == 0) {
                            capacidadPorComida.get(tipoComida).remove(mesa);
                        }
                    }

                    // Se eliminan los horarios que ya pasaron si la fecha es de hoy
                    if (d.isEqual(LocalDate.now())) {
                        horariosGenerados = horariosGenerados
                                .stream()
                                .filter(h -> h.getHorario().isAfter(LocalTime.now()))
                                .toList();
                    }
                    Map<String, HorarioConCapacidadDisponible> horariosConCapacidad = new LinkedHashMap<>();
                    for (Map.Entry<TipoComida, Set<Mesa>> entry : capacidadPorComida.entrySet()) {
                        TipoComida tipoComida = entry.getKey();
                        Set<Mesa> mesas = entry.getValue();

                        HorarioConCapacidadDisponible horarioConCapacidad = HorarioConCapacidadDisponible
                                .builder()
                                .tipoComida(tipoComida)
                                .horarios(horariosGenerados
                                        .stream()
                                        .filter(hc -> hc.getTipoComida().equals(tipoComida))
                                        .map(Horario::getHorario)
                                        .toList())
                                .mesas(new ArrayList<>(mesas))
                                .build();
                        if (!horarioConCapacidad.getHorarios().isEmpty() && !horarioConCapacidad.getMesas().isEmpty()) {
                            horariosConCapacidad.put(tipoComida.toString(), horarioConCapacidad);
                        }
                    }

                    if (!horariosConCapacidad.isEmpty())
                        horarios.put(d, horariosConCapacidad);

                    break;
                }
            }
        }

        return horarios;
    }

    @Override
    public Map<LocalDate, Tuple<List<Horario>, ConfiguradorHorario>> horariosEnMesDisponiblesSegunMesAnioConConfiguradorCoincidente(String correoRestaurante, YearMonth mesAnio) {
        Optional<HorarioPorRestaurante> horarioPorRestaurante = horarioPorRestauranteService
                .getByCorreoRestaurante(correoRestaurante);
        if (horarioPorRestaurante.isEmpty()) {
            return new LinkedHashMap<>();
        }

        Collection<ConfiguradorHorario> configuradoresHorario = horarioPorRestaurante
                .get()
                .getConfiguradores()
                .values();

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
                throw new IllegalStateException(
                        "El restaurante con ID " + restaurante.getIdRestaurante() + " no existe.");
            }

            QueryParams queryParams = new QueryParams();
            queryParams.addFilter(Filter.equalTo("idRestaurante", restaurante.getIdRestaurante()));
            queryParams.addFilter(Filter.equalTo("idUsuario", restaurante.getIdUsuario()));

            List<RestauranteUsuario> restaurantesVistosDelUsuario = restauranteVistoRecientementeDao
                    .getByParams(queryParams);

            if (!restaurantesVistosDelUsuario.isEmpty()) {
                return restaurantesVistosDelUsuario.get(0);
            }

            restauranteVistoRecientementeDao.save(restaurante, idRestaurante);

            List<RestauranteUsuario> restaurantes = restauranteVistoRecientementeDao
                    .getFiltered(
                            Filter.equalTo("idUsuario", restaurante.getIdUsuario()));
            // Ordena segun fechaGuardado para eliminar el mas antiguo (si hay mas de 5)
            restaurantes.sort(Comparator.comparing(r -> {
                try {
                    String fechaGuardado = ((RestauranteUsuario) r).getFechaGuardado();
                    long seconds = Long
                            .parseLong(
                                    fechaGuardado.substring(
                                            fechaGuardado.indexOf('=') + 1, fechaGuardado.indexOf(',')));
                    int nanos = Integer
                            .parseInt(
                                    fechaGuardado
                                            .substring(fechaGuardado.lastIndexOf('=') + 1, fechaGuardado.indexOf(')')));

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

    @Override
    public List<String> addFavorito(String idRestaurante, RestauranteUsuario restauranteFavorito, String idDetalleUsuario) {
        try {
            if (restauranteDao.get(restauranteFavorito.getIdRestaurante()) == null) {
                throw new IllegalStateException(
                        "El restaurante con ID " + restauranteFavorito.getIdRestaurante() + " no existe.");
            }
            QueryParams queryParams = new QueryParams();
            queryParams.addFilter(Filter.equalTo("idRestaurante", restauranteFavorito.getIdRestaurante()));
            queryParams.addFilter(Filter.equalTo("idUsuario", restauranteFavorito.getIdUsuario()));

            if (!restauranteFavoritoDao.getByParams(queryParams).isEmpty()) {
                throw new IllegalStateException(
                        "El restaurante con ID " + restauranteFavorito
                                .getIdRestaurante() + " ya existe para el usuario con ID " + restauranteFavorito
                                .getIdUsuario() + ".");
            }

            restauranteFavoritoDao.save(restauranteFavorito, idRestaurante);

            // Siempre se guarda con el idRestaurante de la colección 'restaurantes'
            return detalleUsuarioDao
                    .addFavorito(idDetalleUsuario, restauranteFavorito.getIdRestaurante());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> removeFavorito(String idRestaurante, String idUsuario, String idDetalleUsuario) {
        try {
            QueryParams queryParamsForNormalRestaurants = new QueryParams();
            queryParamsForNormalRestaurants.addFilter(Filter.equalTo("idRestaurante", idRestaurante));
            queryParamsForNormalRestaurants.addFilter(Filter.equalTo("idUsuario", idUsuario));

            List<RestauranteUsuario> restaurantesFavoritosUsuario = restauranteFavoritoDao
                    .getByParams(queryParamsForNormalRestaurants);

            // Puede ser que la vista del usuario sea de un RestauranteUsuario en vez de un Restaurante común, por lo tanto hay que considerar ambos casos
            if (restaurantesFavoritosUsuario.isEmpty()) {
                QueryParams queryParamsForUserRestaurants = new QueryParams();
                queryParamsForUserRestaurants.addFilter(Filter.equalTo("idUsuario", idUsuario));

                restaurantesFavoritosUsuario = restauranteFavoritoDao
                        .getByParams(queryParamsForUserRestaurants)
                        .stream()
                        .filter(r -> r.getId().equals(idRestaurante))
                        .toList();

                if (restaurantesFavoritosUsuario.isEmpty()) {
                    throw new IllegalStateException(
                            "El restaurante con ID " + idRestaurante + " no existe para el usuario con ID " + idUsuario + ".");
                }
            }

            restauranteFavoritoDao.delete(restaurantesFavoritosUsuario.get(0).getId());
            return detalleUsuarioDao
                    .removeFavorito(idDetalleUsuario, restaurantesFavoritosUsuario.get(0).getIdRestaurante());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Restaurante> getRestaurantesEnArea(LatLng northeast, LatLng southwest) {
        try {
            QueryParams queryParams = new QueryParams();
            queryParams.addFilter(Filter.equalTo("estado", EstadoRestaurante.HABILITADO.toString()));
            queryParams.addFilter(Filter.greaterThan("latitud", southwest.getLatitude()));
            queryParams.addFilter(Filter.lessThan("latitud", northeast.getLatitude()));
            queryParams.addFilter(Filter.greaterThan("longitud", southwest.getLongitude()));
            queryParams.addFilter(Filter.lessThan("longitud", northeast.getLongitude()));
            return restauranteDao.getByParams(queryParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Restaurante> getDestacados() {
        try {
            return restauranteDao.getFiltered(Filter.greaterThanOrEqualTo("puntuacion", 4.0));
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
