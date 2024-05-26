package com.barapp.web.views.utils.validation;

import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioDiaEspecifico;
import com.barapp.web.model.ConfiguradorHorarioSemanal;

public abstract class ConfiguradorHorarioValidator {
    public boolean colisiona(ConfiguradorHorario ch) {
        return ch instanceof ConfiguradorHorarioDiaEspecifico cfDia && this.colisiona(cfDia)
                || ch instanceof ConfiguradorHorarioSemanal cfSem && this.colisiona(cfSem);
    }

    public abstract boolean colisiona(ConfiguradorHorarioDiaEspecifico chEspecifico);
    public abstract boolean colisiona(ConfiguradorHorarioSemanal chSemanal);
}
