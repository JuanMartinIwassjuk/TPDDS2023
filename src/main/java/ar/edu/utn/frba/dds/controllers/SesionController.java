package ar.edu.utn.frba.dds.controllers;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class SesionController implements ICrudViewsHandler {

  RepositorioIncidentes repositorioIncidentes;

  public SesionController(RepositorioIncidentes repositorioIncidentes) {
    this.repositorioIncidentes = repositorioIncidentes;
  }

  @Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {
    if (Boolean.TRUE.equals(context.sessionAttribute("user_id"))) {
      context.redirect("/");
    }

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("SesionIniciada", false);
    modelo.put("RolAdministrador", false);
    modelo.put("RolMiembro", false);

    context.render("IniciarSesion.hbs", modelo);

  }

  @Override
  public void create(Context context) {

    try {
      Usuario usuario = RepositorioUsuarios.getInstance().buscarPorUsuarioYcontrasenia(
              context.formParams("username"),
              context.formParams("password")
      );

      context.sessionAttribute("user_rol", usuario.getRol().getNombre());
      context.sessionAttribute("user_id", usuario.getId().toString());

      context.redirect("/");


    } catch (NoSuchElementException e) {
      context.redirect("/iniciarsesion");

    }
  }






  @Override
  public void save(Context context) {

  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }
}
