package ar.edu.utn.frba.dds.models.entidades.notificaciones;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;

public class MailNotifier implements Notificador {

  MailSender mailSender;

  @Override
  public void enviarNotificacion(Usuario usuario, Notificacion notificacion) {
   mailSender.enviarNotificacion(usuario.getMail(), notificacion);
  }

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }
}
