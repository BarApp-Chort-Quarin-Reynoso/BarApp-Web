package com.barapp.web.views.testing;

import com.barapp.web.business.service.*;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.utils.TestConsts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
@PageTitle("Fake View")
@Route(value = "fakeview")
@AnonymousAllowed
public class FakeView extends VerticalLayout implements HasUrlParameter<String> {

    private final UsuarioWebService usuarioWebService;
    private final RestauranteService restauranteService;
    private final DetalleRestauranteService detalleRestauranteService;
    private final HorarioPorRestauranteService horarioPorRestauranteService;
    private final OpinionService opinionService;
    private final EstadisticaService estadisticaService;

    Span returnSpan = new Span();

    public FakeView(UsuarioWebService usuarioWebService, RestauranteService restauranteService, DetalleRestauranteService detalleRestauranteService, HorarioPorRestauranteService horarioPorRestauranteService, OpinionService opinionService, EstadisticaService estadisticaService) {
        this.usuarioWebService = usuarioWebService;
        this.restauranteService = restauranteService;
        this.detalleRestauranteService = detalleRestauranteService;
        this.horarioPorRestauranteService = horarioPorRestauranteService;
        this.opinionService = opinionService;
        this.estadisticaService = estadisticaService;

        H1 title = new H1("This is a fake view");

        returnSpan.setId("return-span");

        add(title, returnSpan);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (parameter != null) {
            List<String> queryParams = event.getLocation().getQueryParameters().getParameters("params");

            for (Method m : this.getClass().getDeclaredMethods()) {
                if (m.getName().equals(parameter) && m.getParameterCount() == queryParams.size()) {
                    try {
                        m.invoke(this, queryParams.toArray());
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exc) {
                        throw new RuntimeException(exc);
                    }
                }
            }
        }
    }

    private void execute(String method) {
        UI.getCurrent().getPage().executeJs(String.format("""
                  setTimeout(() => {
                    $0.$server.%s();
                  });
                """, method), this.getElement());
    }

    private void crearUsuarioBarConEstado(String username, String estadoString) {
        EstadoRestaurante estado = EstadoRestaurante.valueOf(estadoString);
        String correo = username + TestConsts.DOMINIO_CORREO_TEST;
        UsuarioWeb usuarioWeb = UsuarioWeb
                .builder()
                .email(correo)
                .hashedPassword("$2a$10$gzxhNfZ545XcHXyOoKxAq.f.wEoy7vqyXiOIKG5N7QXTSkJJ1zXNi")
                .rol(Rol.BAR)
                .build();
        Restaurante restaurante = Restaurante
                .builder()
                .nombre(username)
                .correo(correo)
                .cuit("99-99999999-9")
                .ubicacion(Ubicacion
                        .builder()
                        .calle("Ejemplo")
                        .numero(123)
                        .nombreLocalidad("Santa Fe")
                        .nombreProvincia("Santa Fe")
                        .nombrePais("Argentina")
                        .build())
                .estado(estado)
                .portada(
                        "https://firebasestorage.googleapis.com/v0/b/barapp-b1bc0.appspot.com/o/images%2Ffotos%2F0afd62a8-7a79-4764-bf4b-b00e8f2e109e.png?alt=media&token=c0aaaec8-f1bc-4170-a0c5-e4a12dec0cbc")
                .logo("https://firebasestorage.googleapis.com/v0/b/barapp-b1bc0.appspot.com/o/images%2Flogos%2F0afd62a8-7a79-4764-bf4b-b00e8f2e109e.png?alt=media&token=d28a0d36-e684-4a39-9c56-1cbdc5727d4b")
                .build();
        DetalleRestaurante detalleRestaurante = DetalleRestaurante
                .builder()
                .idRestaurante(restaurante.getId())
                .build();
        restaurante.setIdDetalleRestaurante(detalleRestaurante.getId());
        Estadistica estadistica = Estadistica
                .builder()
                .idRestaurante(restaurante.getId())
                .correo(correo)
                .build();

        try {
            restauranteService.save(restaurante, restaurante.getId());
            usuarioWebService.save(usuarioWeb, usuarioWeb.getId());
            detalleRestauranteService.save(detalleRestaurante, detalleRestaurante.getId());
            estadisticaService.save(estadistica);

            returnSpan.setText(correo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void eliminarUsuarioBar(String correo) {
        UsuarioWeb usuarioWeb = usuarioWebService.getByUsername(correo);
        Optional<Restaurante> restauranteOpt = restauranteService.getByCorreo(correo);

        try {
            usuarioWebService.delete(usuarioWeb.getId());
            if (restauranteOpt.isPresent()) {
                restauranteService.delete(restauranteOpt.get().getId());
                detalleRestauranteService.delete(restauranteOpt.get().getIdDetalleRestaurante());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void eliminarOpinion(String id) {
        try {
            opinionService.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void crearOpinion(String correoBar, String comentario, String puntuacion) {
        Restaurante restaurante = restauranteService.getByCorreo(correoBar).orElseThrow();

        int puntuacionInt = Integer.parseInt(puntuacion);
        double newPuntuacion = ((restaurante.getPuntuacion() * restaurante.getCantidadOpiniones()) + puntuacionInt)
                / (restaurante.getCantidadOpiniones() + 1);
        restaurante.setCantidadOpiniones(restaurante.getCantidadOpiniones() + 1);
        restaurante.setPuntuacion(newPuntuacion);

        Opinion opinion = Opinion
                .builder()
                .idRestaurante(restaurante.getId())
                .usuario(UsuarioApp.builder()
                        .nombre("Usuario")
                        .apellido("Test")
                        .foto("https://firebasestorage.googleapis.com/v0/b/barapp-b1bc0.appspot.com/o/images%2Flogos%2F0afd62a8-7a79-4764-bf4b-b00e8f2e109e.png?alt=media&token=d28a0d36-e684-4a39-9c56-1cbdc5727d4b")
                        .build())
                .comentario(comentario)
                .nota(Double.parseDouble(puntuacion))
                .fecha(LocalDate.now().minusMonths(1))
                .horario(Horario
                        .builder()
                        .horario(LocalTime.now())
                        .tipoComida(TipoComida.CENA)
                        .build())
                .build();
        try {
            opinionService.save(opinion);
            restauranteService.save(restaurante);
            returnSpan.setText(opinion.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
