package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.entidadescsv.OrganismoDeControl;
import ar.edu.utn.frba.dds.models.entidades.entidadescsv.ServicioCargaCSV;
import ar.edu.utn.frba.dds.models.entidades.servicios.EscaleraMecanica;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioDeServicios;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioOrganismosControl;
import ar.edu.utn.frba.dds.server.Server;
import ar.edu.utn.frba.dds.server.exceptions.AccessDeniedException;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrganismosController extends Controller implements ICrudViewsHandler {

  RepositorioOrganismosControl repositorioOrganismosControl = new RepositorioOrganismosControl();

  public OrganismosController(RepositorioOrganismosControl repositorioOrganismosControl) {
    this.repositorioOrganismosControl = repositorioOrganismosControl;
  }

  @Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }

    Map<String, Object> model = new HashMap<>();
    context.render("cargaEntidades.hbs", model);
  }

  @Override
  public void create(Context context) {

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

  public void cargaCSV(Context context) throws ServletException, IOException {
    if (!Objects.isNull(context.contentType()) && context.contentType().contains("multipart")) {

      long maxFileSize = 5242880;
      long maxRequestSize = 10485760;
      int fileSizeThreshold = 1024;

      MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
          null, maxFileSize, maxRequestSize, fileSizeThreshold);
      context.req().setAttribute("org.eclipse.jetty.multipartConfig",
          multipartConfigElement);
      //request.raw().setAttribute("org.eclipse.jetty.multipartConfig",
         // multipartConfigElement);
    }
    //Part p = request.raw().getPart("fileSelect");
    Part p = context.req().getPart("fileSelect");

    byte[] file = null;
    try {
      InputStream inputStream = p.getInputStream();
      file = IOUtils.toByteArray(inputStream);
    } catch (final IOException e) {
    }

    ServicioCargaCSV servicioCargaDatos= new ServicioCargaCSV();
    List<OrganismoDeControl> organismoDeControls = servicioCargaDatos.convertCSVFromBytes(file);
    System.out.println(organismoDeControls.get(0).getNombreOrganismo());

    //withTransaction(() -> {
      //DatosActividadRepositorio.getInstance().guardarListaDatoActividad(organismoDeControls);
      repositorioOrganismosControl.guardarOrganismos(organismoDeControls);
    //});


    List<OrganismoDeControl> organismosControl = repositorioOrganismosControl.buscarTodos();

    System.out.println(organismosControl.get(0).getNombreOrganismo());
    System.out.println(organismosControl.get(0).getEmpresaPrestadora().getNombreEmpresa());
    System.out.println(organismosControl.get(1).getNombreOrganismo());
    System.out.println(organismosControl.get(1).getEmpresaPrestadora().getNombreEmpresa());

    Map<String, Object> model = new HashMap<>();
    //model.put("datosActividad", organismosControl);
    context.redirect("/");
  }

  public void listadoOrganismos(Context context) {
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }


    List<OrganismoDeControl> organismos = repositorioOrganismosControl.buscarTodos();


    Map<String, Object> model = new HashMap<>();
    model.put("organismos", organismos);
    context.render("listadoOrganismos.hbs", model);
  }







}

