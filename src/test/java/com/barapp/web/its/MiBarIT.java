package com.barapp.web.its;

import com.barapp.web.BaseIT;
import com.barapp.web.elements.views.LoginViewPO;
import com.barapp.web.elements.views.MiBarViewPO;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.utils.TestConsts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MiBarIT extends BaseIT {
    private Set<String> usersCreated = new HashSet<>();

    public MiBarIT() {
    }

    @AfterEach
    protected void afterEach() {
        usersCreated.forEach(this::eliminarUsuarioBar);
        usersCreated.clear();
    }

    @Test
    @DisplayName("Cargar campos del formulario")
    public void testValidaciones() {
        String correo = crearBarConEstado("testbar", EstadoRestaurante.HABILITADO);
        usersCreated.add(correo);

        LoginViewPO loginView = new LoginViewPO(page);
        loginView.navigate();
        loginView.login(correo, TestConsts.PASSWORD_TEST);

        MiBarViewPO miBarView = new MiBarViewPO(page);
        miBarView.navigate();

        // Campo Nombre del Bar
        miBarView.getFormTextInput("Nombre del bar").fill("Nombre demasiado largo que no deberia ser valido nunca");
        page.locator("main").click();
        assertThat(miBarView.getFormTextInputErrorLabel("Nombre del bar")).isVisible();
        miBarView.getFormTextInput("Nombre del bar").fill("Nombre apropiado");
        page.locator("main").click();
        assertThat(miBarView.getFormTextInputErrorLabel("Nombre del bar")).not().isVisible();

        // Campo Telefono
        miBarView.getFormTextInput("Teléfono").fill("12345678901234567890");
        page.locator("main").click();
        assertThat(miBarView.getFormTextInput("Teléfono")).hasValue("12345678901234");

        // Campo CUIT
        miBarView.getFormTextInput("CUIT").clear();
        miBarView.getFormTextInput("CUIT").fill("204186402601");
        page.locator("main").click();
        assertThat(miBarView.getFormTextInput("CUIT")).hasValue("20-41864026-0");

        // Campo Link Menu
        miBarView.getFormTextInput("Link a tu menú").fill("abgfhdjrekrkrkldldlfñepprolflflldkdkf");
        page.locator("main").click();
        assertThat(miBarView.getFormTextInputErrorLabel("Link a tu menú")).isVisible();
        assertThat(miBarView.getFormTextInputErrorLabel("Link a tu menú")).hasText(
                "El link a tu menú debe ser una url válida");
        miBarView.getFormTextInput("Link a tu menú").fill("https://www.youtube.com/");
        page.locator("main").click();
        assertThat(miBarView.getFormTextInputErrorLabel("Link a tu menú")).not().isVisible();

        // Campo Dirección
        miBarView.getFormTextInput("Dirección").clear();
        page.locator("main").click();
        assertThat(miBarView.getFormTextInputErrorLabel("Dirección")).isVisible();
        assertThat(miBarView.getFormTextInputErrorLabel("Dirección")).hasText("El campo es obligatorio");
        miBarView.getFormTextInput("Dirección").fill("a");
        page.locator("main").click();
        assertThat(miBarView.getFormTextInputErrorLabel("Dirección")).isVisible();
        assertThat(miBarView.getFormTextInputErrorLabel("Dirección")).hasText("Debe tener entre 5 y 50 caracteres");
        miBarView.getFormTextInput("Dirección").fill("Direccion demasiado larga para que no pase la validacion nunca");
        page.locator("main").click();
        assertThat(miBarView.getFormTextInputErrorLabel("Dirección")).isVisible();
        assertThat(miBarView.getFormTextInputErrorLabel("Dirección")).hasText("Debe tener entre 5 y 50 caracteres");
        miBarView.getFormTextInput("Dirección").fill("Direccion correcta");
        page.locator("main").click();
        assertThat(miBarView.getFormTextInputErrorLabel("Dirección")).not().isVisible();


        // Campo Número
        miBarView.getFormIntegerInput("Número").clear();
        page.locator("main").click();
        assertThat(miBarView.getFormIntegerInputErrorLabel("Número")).isVisible();
        assertThat(miBarView.getFormIntegerInputErrorLabel("Número")).hasText("El campo es obligatorio");
        miBarView.getFormIntegerInput("Número").fill("1234");
        page.locator("main").click();
        assertThat(miBarView.getFormIntegerInput("Número")).hasValue("1234");
        assertThat(miBarView.getFormIntegerInputErrorLabel("Número")).not().isVisible();

        miBarView.clickGuardar();
        page.reload();

        assertThat(miBarView.getFormTextInput("Nombre del bar")).hasValue("Nombre apropiado");
        assertThat(miBarView.getFormTextInput("Teléfono")).hasValue("12345678901234");
        assertThat(miBarView.getFormTextInput("CUIT")).hasValue("20-41864026-0");
        assertThat(miBarView.getFormTextInput("Link a tu menú")).hasValue("https://www.youtube.com/");
        assertThat(miBarView.getFormIntegerInput("Número")).hasValue("1234");
    }
}
