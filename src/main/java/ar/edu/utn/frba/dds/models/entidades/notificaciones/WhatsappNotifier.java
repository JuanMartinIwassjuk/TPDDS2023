package ar.edu.utn.frba.dds.models.entidades.notificaciones;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;

public class WhatsappNotifier implements Notificador {

  WhatsappSender whatsappSender;

  @Override
  public void enviarNotificacion(Usuario usuario, Notificacion notificacion) {
      whatsappSender.enviarNotificacion(usuario.getCelular(), notificacion);
  }
}
