package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.rankings.GeneradorDeRanking;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioMiembros;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioOrganismosControl;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioPrestaciones;

public class FactoryController {

  public static Object controller(String nombre) {
    Object controller = null;
    switch (nombre) {
      case "Menu" : controller = new MenuController(); break;
      case "Incidentes" : controller = new IncidenteController(new RepositorioPrestaciones(),new RepositorioComunidades(), new RepositorioIncidentes(), new RepositorioMiembros()); break;
      case "Sesiones" : controller = new SesionController(new RepositorioIncidentes()); break;
      case "Organismos" : controller = new OrganismosController(new RepositorioOrganismosControl()); break;
      case "Rankings" : controller = new EntidadesController(new GeneradorDeRanking());
    }
    return controller;
  }
}
