package ar.edu.utn.frba.dds.models.entidades.modosnotificacion;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class NotificadorSinApuros implements ModoNotificacion {

  List<Horario> horarios;

  @Override
  public Boolean estaEnRangoHorario(LocalDateTime fecha) {
    return this.horarios.stream().anyMatch(horario -> fecha.isAfter(horario.getHoraInicio()) && fecha.isBefore(horario.getHoraFin()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NotificadorSinApuros that = (NotificadorSinApuros) o;
    return Objects.equals(horarios, that.horarios);
  }

  @Override
  public int hashCode() {
    return Objects.hash(horarios);
  }


}

