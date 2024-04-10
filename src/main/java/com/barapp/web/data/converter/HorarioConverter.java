package com.barapp.web.data.converter;

import com.barapp.web.data.entities.HorarioEntity;
import com.barapp.web.model.Horario;
import com.barapp.web.model.TipoComida;

import java.time.LocalTime;
import java.util.List;

public class HorarioConverter {

    public static HorarioEntity toEntity(Horario dto) {
        HorarioEntity entity = HorarioEntity.builder()
                                            .idHorario(dto.getId())
                                            .hora(dto.getHora().toString())
                                            .tipoComida(dto.getTipoComida().toString())
                                            .build();

        return entity;
    }

    public static Horario toDto(HorarioEntity entity) {
        Horario horario = Horario.builder()
                                 .hora(LocalTime.parse(entity.getHora()))
                                 .tipoComida(TipoComida.valueOf(entity.getTipoComida()))
                                 .build();

        horario.setId(entity.getIdHorario());
        return horario;
    }

    public static List<HorarioEntity> toEntityList(List<Horario> listaHorarios) {
        return listaHorarios.stream().map(HorarioConverter::toEntity).toList();
    }

    public static List<Horario> toModelList(List<HorarioEntity> listaHorariosEntity) {
        return listaHorariosEntity.stream().map(HorarioConverter::toDto).toList();
    }
}
