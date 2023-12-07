package ar.edu.utn.frba.dds.models.entidades.modosnotificacion;

import java.time.LocalDateTime;
import javax.persistence.Converter;


public interface ModoNotificacion {
    Boolean estaEnRangoHorario(LocalDateTime fecha);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

}


