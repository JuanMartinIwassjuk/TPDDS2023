package ar.edu.utn.frba.dds.models.entidades.modosnotificacion;

import java.time.LocalDateTime;
import java.util.Objects;

public class NotificadorEnInstante implements ModoNotificacion {


  @Override
  public Boolean estaEnRangoHorario(LocalDateTime fecha) {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return true; // En este caso, consideramos que todos los objetos de esta clase son iguales
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass()); // Usamos la clase para generar el hashCode
  }


}
