package ar.edu.utn.frba.dds.models.entidades.modosnotificacion;

import java.time.LocalDateTime;
import javax.persistence.Converter;

@Converter
public interface ModoNotificacion {
    Boolean estaEnRangoHorario(LocalDateTime fecha);
    @Override
    public boolean equals(Object o);

}
