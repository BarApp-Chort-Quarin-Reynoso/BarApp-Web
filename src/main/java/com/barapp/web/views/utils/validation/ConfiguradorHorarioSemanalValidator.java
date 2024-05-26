package com.barapp.web.views.utils.validation;

import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioDiaEspecifico;
import com.barapp.web.model.ConfiguradorHorarioSemanal;
import com.barapp.web.model.enums.TipoComida;
import lombok.Getter;

import java.time.DayOfWeek;

@Getter
public class ConfiguradorHorarioSemanalValidator extends ConfiguradorHorarioValidator {
    ConfiguradorHorarioSemanal nuevoConfigurador;

    public ConfiguradorHorarioSemanalValidator(ConfiguradorHorarioSemanal nuevoConfigurador) {
        this.nuevoConfigurador = nuevoConfigurador;
    }

    @Override
    public boolean colisiona(ConfiguradorHorarioDiaEspecifico chEspecifico) {
        return false;
    }

    @Override
    public boolean colisiona(ConfiguradorHorarioSemanal chSemanal) {
        for (DayOfWeek d : nuevoConfigurador.getDaysOfWeek()) {
            if (chSemanal.getDaysOfWeek().contains(d)) {
                return true;
            }
        }

        return false;
    }
}
