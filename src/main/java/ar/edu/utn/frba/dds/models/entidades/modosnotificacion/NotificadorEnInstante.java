package ar.edu.utn.frba.dds.models.entidades.modosnotificacion;

import java.time.LocalDateTime;

public class NotificadorEnInstante implements ModoNotificacion {


  @Override
  public Boolean estaEnRangoHorario(LocalDateTime fecha) {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    // Lógica de igualdad personalizada aquí
    return true;
  }


}
