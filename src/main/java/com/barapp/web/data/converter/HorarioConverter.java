package com.barapp.web.data.converter;

import com.barapp.web.data.entities.HorarioEntity;
import com.barapp.web.model.Horario;
import com.barapp.web.model.enums.TipoComida;

import java.time.LocalTime;
import java.util.List;

public class HorarioConverter {

    public static HorarioEntity toEntity(Horario dto) {
        HorarioEntity entity = HorarioEntity.builder()
                                            .hora(dto.getHorario().toString())
                                            .tipoComida(dto.getTipoComida().toString())
                                            .build();

        return entity;
    }

    public static Horario toDto(HorarioEntity entity) {
        Horario horario = Horario.builder()
                                 .horario(LocalTime.parse(entity.getHora()))
                                 .tipoComida(TipoComida.valueOf(entity.getTipoComida()))
                                 .build();
        return horario;
    }

    public static List<HorarioEntity> toEntityList(List<Horario> listaHorarios) {
        return listaHorarios.stream().map(HorarioConverter::toEntity).toList();
    }

    public static List<Horario> toModelList(List<HorarioEntity> listaHorariosEntity) {
        return listaHorariosEntity.stream().map(HorarioConverter::toDto).toList();
    }
}
