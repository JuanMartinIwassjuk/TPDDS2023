package ar.edu.utn.frba.dds.server;


import static io.javalin.apibuilder.ApiBuilder.*;

import ar.edu.utn.frba.dds.controllers.FactoryController;
import ar.edu.utn.frba.dds.controllers.IncidenteController;
import ar.edu.utn.frba.dds.controllers.MenuController;
import ar.edu.utn.frba.dds.controllers.OrganismosController;
import ar.edu.utn.frba.dds.controllers.SesionController;
import ar.edu.utn.frba.dds.controllers.EntidadesController;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import io.javalin.Javalin;

public class Router {

  public static void init() {


    Server.app().routes(() -> {
      get("/",((MenuController) FactoryController.controller("Menu"))::index);
      get("/menu", context -> context.render("Menu_Inicio.hbs"));
      get("/iniciarsesion", ((SesionController) FactoryController.controller("Sesiones"))::show);
      post("/iniciarsesion", ((SesionController) FactoryController.controller("Sesiones"))::create);
      get("/incidentesenlocalizacion", ((IncidenteController) FactoryController.controller("Incidentes"))::mostrarIncidentePorLocalizacionDeUsuario);
      get("incidentesAbiertos", ((IncidenteController) FactoryController.controller("Incidentes"))::mostrarIncidentePorMiembroAsociado);
      post("/cerrarIncidentes", ((IncidenteController) FactoryController.controller("Incidentes"))::cerrarIncidentes);
      get("/incidentes/nuevo",((IncidenteController) FactoryController.controller("Incidentes"))::show);
      post("/incidentes/nuevo",((IncidenteController) FactoryController.controller("Incidentes"))::create);
      get("/cargaOrganismos", ((OrganismosController) FactoryController.controller("Organismos"))::show);
      post("/cargaOrganismos", ((OrganismosController) FactoryController.controller("Organismos"))::cargaCSV);
      get("/organismos", ((OrganismosController) FactoryController.controller("Organismos"))::listadoOrganismos);
      get("/rankings", ((EntidadesController) FactoryController.controller("Rankings"))::index);






      /*
      //get("servicios", ((ServiciosController) FactoryController.controller("Servicios"))::index);
      //get("servicios/crear", ((ServiciosController) FactoryController.controller("Servicios"))::create, TipoRol.ADMINISTRADOR);
      //get("servicios/{id}", ((ServiciosController) FactoryController.controller("Servicios"))::show);
      //get("servicios/{id}/editar", ((ServiciosController) FactoryController.controller("Servicios"))::edit);
      //post("servicios/{id}", ((ServiciosController) FactoryController.controller("Servicios"))::update);
      //post("servicios", ((ServiciosController) FactoryController.controller("Servicios"))::save);
      //delete("servicios/{id}", ((ServiciosController) FactoryController.controller("Servicios"))::delete);
      //path("servicios/{id}/tareas", () -> {
        //get(((TareasController) FactoryController.controller("Tareas"))::index);

    //});

      */

    });
  }
}