package com.barapp.web.views.utils.validation;

import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioDiaEspecifico;
import com.barapp.web.model.ConfiguradorHorarioSemanal;
import lombok.Getter;

@Getter
public class ConfiguradorHorarioDiaEspecificoValidator extends ConfiguradorHorarioValidator {
    ConfiguradorHorarioDiaEspecifico configuradorHorario;

    public ConfiguradorHorarioDiaEspecificoValidator(ConfiguradorHorarioDiaEspecifico configuradorHorario) {
        this.configuradorHorario = configuradorHorario;
    }

    @Override
    public boolean colisiona(ConfiguradorHorarioDiaEspecifico chEspecifico) {
        return configuradorHorario.getFecha().equals(chEspecifico.getFecha());
    }

    @Override
    public boolean colisiona(ConfiguradorHorarioSemanal chSemanal) {
        return false;
    }
}
