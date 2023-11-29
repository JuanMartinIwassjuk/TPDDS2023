package ar.edu.utn.frba.dds.models.entidades.notificaciones;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;

public interface Notificador {
  void enviarNotificacion(Usuario usuario, Notificacion notificacion);
}
