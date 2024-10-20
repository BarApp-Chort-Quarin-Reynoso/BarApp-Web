package com.barapp.web.its;

import com.barapp.web.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class MisHorariosIT extends BaseIT {
    private Set<String> usersCreated = new HashSet<>();

    protected MisHorariosIT() {
        super();
    }

    @AfterEach
    protected void afterEach() {
        usersCreated.forEach(this::eliminarUsuarioBar);
        usersCreated.clear();
    }

    @Test
    @DisplayName("Crear un nuevo horario semanal")
    public void testCrearHorarioSemanal() {

    }

    @Test
    @DisplayName("Crear un nuevo evento")
    public void testCrearEvento() {

    }

    @Test
    @DisplayName("Crear un nuevo feriado")
    public void testCrearFeriado() {

    }

    @Test
    @DisplayName("Eliminar un configurador")
    public void testEliminarConfigurador() {

    }

    @Test
    @DisplayName("Editar un configurador")
    public void testEditarConfigurador() {

    }

    @Test
    @DisplayName("Editar la capacidad por defecto")
    public void testEditarCapacidad() {

    }

    @Test
    @DisplayName("Testear formulario de creacion de configuradores")
    public void testProbarFormularioConfigurador() {

    }
}
