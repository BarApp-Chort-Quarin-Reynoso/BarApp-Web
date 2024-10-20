package com.barapp.web.its;

import com.barapp.web.BaseIT;
import com.barapp.web.elements.views.RegistrarFormPO;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegistrarIT extends BaseIT {
    private Set<String> usersCreated = new HashSet<>();

    public RegistrarIT() {
    }

    @AfterEach
    protected void afterEach() {
        usersCreated.forEach(this::eliminarUsuarioBar);
        usersCreated.clear();
    }

    @Test
    @DisplayName("Registrar un bar")
    public void testRegistrarBar() {
        RegistrarFormPO registrarForm = new RegistrarFormPO(page);
        registrarForm.navigate();

        usersCreated.add("barprueba@gmail.com");

        page.getByLabel("Nombre del bar").fill("Bar de prueba");
        page.getByLabel("Dirección").fill("Av. Test");
        page.getByLabel("Número").fill("1234");
        page.getByLabel("Correo electrónico").fill("barprueba@gmail.com");
        page.getByLabel("Contraseña", new Page.GetByLabelOptions().setExact(true)).fill("12345");
        page.getByLabel("Confirma tu contraseña").fill("12345");
        page.getByLabel("Teléfono").fill("+543411234567");
        page.getByLabel("CUIT").fill("41-12345678-9");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Siguiente")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Siguiente")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Siguiente")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Registra tu bar")).click();

        assertThat(page.getByText("Registro finalizado")).isVisible();
    }
}
