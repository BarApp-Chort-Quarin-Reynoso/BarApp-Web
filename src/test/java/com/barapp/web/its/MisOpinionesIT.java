package com.barapp.web.its;

import com.barapp.web.BaseIT;
import com.barapp.web.elements.views.LoginViewPO;
import com.barapp.web.elements.views.MisOpinionesPO;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.utils.TestConsts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class MisOpinionesIT extends BaseIT {
    private Set<String> usersCreated = new HashSet<>();
    private Set<String> opinionesCreated = new HashSet<>();

    protected MisOpinionesIT() {
        super();
    }

    @AfterEach
    protected void afterEach() {
        opinionesCreated.forEach(this::eliminarOpinion);
        opinionesCreated.clear();

        usersCreated.forEach(this::eliminarUsuarioBar);
        usersCreated.clear();
    }

    @Test
    @DisplayName("Probar que se muestra la lista de opiniones")
    public void testListaOpiniones() {
        String correo = crearBarConEstado("opiniones", EstadoRestaurante.HABILITADO);
        usersCreated.add(correo);

        LoginViewPO loginView = new LoginViewPO(page);
        loginView.navigate();
        loginView.login(correo, TestConsts.PASSWORD_TEST);

        MisOpinionesPO opinionesPO = new MisOpinionesPO(page);
        opinionesPO.navigate();

        crearOpinion(correo, "opinion1", 5);

        page.reload();
    }
}
