package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.models.entidades.common.Obstaculo;
import ar.edu.utn.frba.dds.models.entidades.common.Permiso;
import ar.edu.utn.frba.dds.models.entidades.common.Rol;
import ar.edu.utn.frba.dds.models.entidades.common.TipoRol;
import ar.edu.utn.frba.dds.models.entidades.common.Tramo;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.entidades.comunidades.ModoUsuario;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.NotificadorEnInstante;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.JavaMailSender;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.MailNotifier;
import ar.edu.utn.frba.dds.models.entidades.servicios.Ascensor;
import ar.edu.utn.frba.dds.models.entidades.servicios.Banio;
import ar.edu.utn.frba.dds.models.entidades.servicios.EscaleraMecanica;
import ar.edu.utn.frba.dds.models.entidades.servicios.EstadoServicio;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.entidades.servicios.TipoBanio;
import ar.edu.utn.frba.dds.models.entidades.servicios.TipoServicio;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.Localizacion;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.LocalizadorProvincia;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Entidad;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Establecimiento;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.TipoEntidad;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.TipoEstablecimiento;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class Initializer implements WithSimplePersistenceUnit {


  public static void init(EntityManager em) throws IOException {

    new Initializer()
        .iniciarTransaccion(em)
        .guardarUsuarios(em)
        .guardarTramos(em)
        .guardarServicios(em)
        .guardarMiembro(em)
        .guardarMiembro2(em)
        .permisos(em)
        //.roles()
        .commitearTransaccion(em);
  }

  private Initializer iniciarTransaccion(EntityManager em) {
    em.getTransaction().begin();
    return this;
  }

  private Initializer commitearTransaccion(EntityManager em) {
    em.getTransaction().commit();
    return this;
  }



  private Initializer permisos(EntityManager em) {
    String[][] permisos = {
        { "Ver servicios", "ver_servicios" },
        { "Crear servicios", "crear_servicios" },
        { "Editar servicios", "editar_servicios" },
        { "Eliminar servicios", "eliminar_servicios" },
    };

    for(String[] unPermiso: permisos) {
      Permiso permiso = new Permiso();
      permiso.setNombre(unPermiso[0]);
      permiso.setNombreInterno(unPermiso[1]);
      em.persist(permiso);
    }

    return this;
  }




  private interface BuscadorDePermisos extends WithSimplePersistenceUnit{
    default Permiso buscarPermisoPorNombre(String nombre,EntityManager em) {
      return (Permiso) em
          .createQuery("from " + Permiso.class.getName() + " where nombreInterno = :nombre")
          .setParameter("nombre", nombre)
          .getSingleResult();
    }
  }



  private Initializer roles(EntityManager em) {
    BuscadorDePermisos buscadorDePermisos = new BuscadorDePermisos() {};

    Rol administrador = new Rol();
    administrador.setNombre("Administrador");
    administrador.setTipo(TipoRol.ADMINISTRADOR);
    administrador.agregarPermisos(
        buscadorDePermisos.buscarPermisoPorNombre("crear_servicios",em)
    );
    em.persist(administrador);

    Rol consumidor = new Rol();
    consumidor.setNombre("Consumidor");
    consumidor.setTipo(TipoRol.NORMAL);
    consumidor.agregarPermisos(
        buscadorDePermisos.buscarPermisoPorNombre("ver_servicios",em)
    );
    em.persist(consumidor);

    Rol prestador = new Rol();
    prestador.setNombre("Prestador");
    prestador.setTipo(TipoRol.NORMAL);
    prestador.agregarPermisos(
        buscadorDePermisos.buscarPermisoPorNombre("ver_servicios",em)
    );
    em.persist(prestador);

    return this;
  }




  private Initializer guardarUsuarios(EntityManager em) {


    List<Permiso> permisosConcedidos = new ArrayList<>();
    Permiso permisoCerrarIncidente = new Permiso();
    permisoCerrarIncidente.setNombre("Permiso de cerrar incidente");
    permisosConcedidos.add(permisoCerrarIncidente);
    Rol rolAdmin = new Rol();
    rolAdmin.agregarPermisos(permisoCerrarIncidente);
    persist(permisoCerrarIncidente);
    persist(rolAdmin);




    Usuario usuarioMatiFdz = new Usuario("matifdz19","conTrAs3NiA%","mati@gmail.com","115215412",rolAdmin);
    persist(usuarioMatiFdz);

    em.persist(rolAdmin);
    em.persist(usuarioMatiFdz);

    return this;
  }


  private Initializer guardarTramos(EntityManager em) {

    List<Obstaculo>obstaculosTramo1 = new ArrayList<>();
    obstaculosTramo1.add(Obstaculo.BARRICADA);
    Tramo tramo1 = new Tramo(obstaculosTramo1);


    List<Obstaculo>obstaculosTramo2 = new ArrayList<>();
    obstaculosTramo1.add(Obstaculo.MOLINETE);
    Tramo tramo2 = new Tramo(obstaculosTramo2);



    em.persist(tramo1);
    em.persist(tramo2);

    return this;

  }




  private Initializer guardarServicios(EntityManager em) {

    List<Obstaculo>obstaculosTramo3 = new ArrayList<>();
    obstaculosTramo3.add(Obstaculo.BARRICADA);
    Tramo tramo3 = new Tramo(obstaculosTramo3);


    List<Obstaculo>obstaculosTramo4 = new ArrayList<>();
    obstaculosTramo4.add(Obstaculo.MOLINETE);
    Tramo tramo4 = new Tramo(obstaculosTramo4);


    List<Obstaculo>obstaculosTramo5= new ArrayList<>();
    obstaculosTramo5.add(Obstaculo.BARRICADA);
    obstaculosTramo5.add(Obstaculo.MOLINETE);
    Tramo tramo5 = new Tramo(obstaculosTramo5);


    List<Obstaculo>obstaculosTramo6 = new ArrayList<>();
    obstaculosTramo6.add(Obstaculo.BARRICADA);
    obstaculosTramo6.add(Obstaculo.MOLINETE);
    Tramo tramo6 = new Tramo(obstaculosTramo6);


    List<Obstaculo>obstaculosTramo7= new ArrayList<>();
    obstaculosTramo7.add(Obstaculo.BARRICADA);
    obstaculosTramo7.add(Obstaculo.MOLINETE);
    Tramo tramo7 = new Tramo(obstaculosTramo7);


    List<Obstaculo>obstaculosTramo8 = new ArrayList<>();
    obstaculosTramo8.add(Obstaculo.BARRICADA);
    obstaculosTramo8.add(Obstaculo.MOLINETE);
    Tramo tramo8 = new Tramo(obstaculosTramo8);


    List<Obstaculo>obstaculosTramo9= new ArrayList<>();
    obstaculosTramo9.add(Obstaculo.BARRICADA);
    obstaculosTramo9.add(Obstaculo.MOLINETE);
    Tramo tramo9 = new Tramo(obstaculosTramo9);


    List<Obstaculo>obstaculosTramo10 = new ArrayList<>();
    obstaculosTramo10.add(Obstaculo.BARRICADA);
    obstaculosTramo10.add(Obstaculo.MOLINETE);
    Tramo tramo10 = new Tramo(obstaculosTramo10);


    Banio banioMujer = new Banio();
    banioMujer.setNombreServicio("Baño mujeres apto para discapacidad");
    banioMujer.setTipoServicio(TipoServicio.BANIO);
    banioMujer.setSexoBanio(TipoBanio.BANIO_MUJER);

    EscaleraMecanica escaleraMecanica = new EscaleraMecanica();
    escaleraMecanica.setNombreServicio("Escalera mecanica de 15 mts");
    escaleraMecanica.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    escaleraMecanica.setCalleAcceso(tramo3);
    escaleraMecanica.setAccesoAnden(tramo4);


    Ascensor ascensor = new Ascensor();
    ascensor.setNombreServicio("Ascensor peso maximo 400kg");
    ascensor.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensor.setAccesoAnden(tramo5);
    ascensor.setCalleAcceso(tramo6);


    Ascensor ascensor2 = new Ascensor();
    ascensor2.setNombreServicio("Ascensor peso maximo 200kg");
    ascensor2.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensor2.setAccesoAnden(tramo7);
    ascensor2.setCalleAcceso(tramo8);

    Ascensor ascensor3 = new Ascensor();
    ascensor3.setNombreServicio("Ascensor peso maximo 300kg");
    ascensor3.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensor3.setAccesoAnden(tramo9);
    ascensor3.setCalleAcceso(tramo10);


    em.persist(tramo3);
    em.persist(tramo4);
    em.persist(tramo5);
    em.persist(tramo6);
    em.persist(tramo7);
    em.persist(tramo8);
    em.persist(tramo9);
    em.persist(tramo10);


    em.persist(ascensor);
    em.persist(escaleraMecanica);
    em.persist(banioMujer);
    em.persist(ascensor2);
    em.persist(ascensor3);

    return this;
  }





  private Initializer guardarMiembro(EntityManager em) throws IOException {


    List<Permiso> permisosConcedidos = new ArrayList<>();
    Permiso permisoAbrirIncidente = new Permiso();
    permisoAbrirIncidente.setNombre("abrir_incidente");
    permisosConcedidos.add(permisoAbrirIncidente);
    Rol rolAdmin = new Rol();
    rolAdmin.setTipo(TipoRol.ADMINISTRADOR);
    rolAdmin.agregarPermisos(permisoAbrirIncidente);
    persist(permisoAbrirIncidente);
    persist(rolAdmin);



    Usuario usuarioJavier = new Usuario("javier48","asd12&iA%","javi@gmail.com","114215426",rolAdmin);
    Localizacion capital = new Localizacion("capitalfederal");
    usuarioJavier.setLocalizacion(capital);

    persist(usuarioJavier);



    Miembro miembroJavier = new Miembro();
    List<Comunidad>comunidadesIntreresadasEnBanioMedrano = new ArrayList<>();



    Banio banioHombre = new Banio();
    banioHombre.setNombreServicio("Baño hombres apto para discapacidad");
    banioHombre.setTipoServicio(TipoServicio.BANIO);
    banioHombre.setSexoBanio(TipoBanio.BANIO_HOMBRE);


    List<Servicio>serviciosMedrano = new ArrayList<>();
    List<Incidente>incidentesMedrano = new ArrayList<>();

    LocalizadorProvincia localizadorProvincia = new LocalizadorProvincia();

    Entidad lineaB = new Entidad();
    lineaB.setNombre("Linea B SUBTE");
    lineaB.setTipoEntidad(TipoEntidad.LINEA);
    lineaB.setIncidentes(incidentesMedrano);
    lineaB.asingarLocalizacion("Buenos Aires",localizadorProvincia);


    List<Establecimiento> estaciones = new ArrayList<>();


    Establecimiento estacionMedrano = new Establecimiento();
    estaciones.add(estacionMedrano);


    estacionMedrano.setServiciosDeEstablecimiento(serviciosMedrano);
    estacionMedrano.setNombre("Estacion Medrano");
    estacionMedrano.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
    estacionMedrano.setEntidad(lineaB);


    lineaB.setEstablecimientos(estaciones);

    Incidente incidenteBanioMedrano = new Incidente();
    incidenteBanioMedrano.setTitulo("Incidente electrico baño Estacion Medrano");
    incidenteBanioMedrano.setDetalle("2 de 6 lamparitas estan quemadas");
    incidenteBanioMedrano.setEstaAbierto(Boolean.TRUE);
    incidenteBanioMedrano.setFechaApertura(LocalDateTime.now());
    incidenteBanioMedrano.setEstaAbierto(Boolean.TRUE);



    Prestacion prestacion = new Prestacion();
    prestacion.setServicio(banioHombre);
    prestacion.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacion.setIncidentes(incidentesMedrano);
    prestacion.setEstablecimiento(estacionMedrano);


    incidenteBanioMedrano.setPrestacion(prestacion);

    incidentesMedrano.add(incidenteBanioMedrano);

    List<Incidente> incidentes = new ArrayList<>();
    List<Comunidad> comunidadesJavier = new ArrayList<>();

    Comunidad comunidadMedrano = new Comunidad();
    comunidadMedrano.setNombre("Vecinos Medrano");
    List<Miembro> miembrosComunidad1 = new ArrayList<>();

    miembrosComunidad1.add(miembroJavier);
    incidenteBanioMedrano.setComunidad(comunidadMedrano);
    comunidadMedrano.setMiembros(miembrosComunidad1);
    incidenteBanioMedrano.setCantidadAfectados(comunidadMedrano.cantidadDeMiembros());

    List<Prestacion> prestacionesDeInteres= new ArrayList<>();
    prestacionesDeInteres.add(prestacion);
    comunidadesIntreresadasEnBanioMedrano.add(comunidadMedrano);
    comunidadMedrano.setPrestacionesDeInteres(prestacionesDeInteres);
    comunidadMedrano.setIncidentesMiembros(incidentesMedrano);
    prestacion.setComunidadesInteresadas(comunidadesIntreresadasEnBanioMedrano);


    comunidadesJavier.add(comunidadMedrano);
    MailNotifier mailNotifier = new MailNotifier();
    mailNotifier.setMailSender(new JavaMailSender());



    miembroJavier.setUsuario(usuarioJavier);
    miembroJavier.setNombre("Javier Omar");
    miembroJavier.setApellido("Lopez");
    miembroJavier.setModoUsuario(ModoUsuario.OBSERVADOR);
    miembroJavier.setNotificadorPreferido(new MailNotifier());
    miembroJavier.setModoNotificacion(new NotificadorEnInstante());
    miembroJavier.setIncidentesSinNotificar(incidentes);
    miembroJavier.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroJavier.setComunidadesQueEsMiembro(comunidadesJavier);



    em.persist(usuarioJavier);
    em.persist(comunidadMedrano);
    em.persist(prestacion);
    em.persist(incidenteBanioMedrano);
    em.persist(lineaB);
    em.persist(estacionMedrano);
    em.persist(miembroJavier);

    return this;

  }



  private Initializer guardarMiembro2(EntityManager em) throws IOException {

    List<Permiso> permisosConcedidos = new ArrayList<>();
    Permiso permisoAbrirIncidente = new Permiso();
    Rol rolAdmin = new Rol();
    Miembro miembroMatias = new Miembro();
    Usuario usuarioMatias = new Usuario();
    List<Comunidad>interesadosEnEscaleraRetiro = new ArrayList<>();
    Banio banioHombre = new Banio();
    List<Servicio>serviciosRetiro = new ArrayList<>();
    List<Incidente>incidentesRetiro = new ArrayList<>();
    LocalizadorProvincia localizadorProvincia = new LocalizadorProvincia();
    Entidad lineaA = new Entidad();
    List<Establecimiento> estaciones = new ArrayList<>();
    Establecimiento estacionMedrano = new Establecimiento();
    Incidente incidenteEscaleraRetiro = new Incidente();
    Prestacion prestacion = new Prestacion();
    List<Incidente> incidentes = new ArrayList<>();
    List<Comunidad> comunidadesMatias = new ArrayList<>();
    Comunidad comunidadVecinosRetiro = new Comunidad();
    List<Miembro> miembrosComunidadRetiro = new ArrayList<>();
    List<Prestacion> prestacionesDeInteres= new ArrayList<>();
    EscaleraMecanica escaleraMecanicaRetiro = new EscaleraMecanica();
    List<Obstaculo>obstaculosTramo1 = new ArrayList<>();
    List<Obstaculo>obstaculosTramo2 = new ArrayList<>();
    Tramo tramo1 = new Tramo();
    Tramo tramo2 = new Tramo();

    //-----------------------

    Comunidad comunidadSubteLineaB = new Comunidad();
    List<Miembro> miembrosComunidadLineaB = new ArrayList<>();







    Miembro miembroJuan = new Miembro();
    Usuario usuarioJuan = new Usuario();
    List<Comunidad> comunidadesJuan = new ArrayList<>();









    //REVISAR ESQUEMA DE ROLES

    permisoAbrirIncidente.setNombre("abrir_incidente");
    permisosConcedidos.add(permisoAbrirIncidente);

    rolAdmin.setTipo(TipoRol.ADMINISTRADOR);
    rolAdmin.agregarPermisos(permisoAbrirIncidente);




    //Seteo de Usuario

    usuarioMatias.setUsuario("matifdz");
    usuarioMatias.setContrasenia("1234");
    usuarioMatias.setMail("mati@gmail.com");
    usuarioMatias.setCelular("1212154");
    usuarioMatias.setRol(rolAdmin);

    usuarioJuan.setUsuario("juaniwassjuk");
    usuarioJuan.setContrasenia("5678");
    usuarioJuan.setMail("juan@gmail.com");
    usuarioJuan.setCelular("321542121");
    usuarioJuan.setRol(rolAdmin);





    //Seteo de Servicio

    obstaculosTramo1.add(Obstaculo.BARRICADA);
    obstaculosTramo1.add(Obstaculo.MOLINETE);
    tramo1.setObstaculos(obstaculosTramo1);
    tramo2.setObstaculos(obstaculosTramo2);

    escaleraMecanicaRetiro.setNombreServicio("Escalera mecanica de 15 mts Retiro");
    escaleraMecanicaRetiro.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    escaleraMecanicaRetiro.setCalleAcceso(tramo1);
    escaleraMecanicaRetiro.setAccesoAnden(tramo2);




    //Seteo de Linea

    lineaA.setNombre("Linea A SUBTE");
    lineaA.setTipoEntidad(TipoEntidad.LINEA);
    lineaA.setIncidentes(incidentesRetiro);
    lineaA.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    lineaA.setEstablecimientos(estaciones);




    //Seteo de Estacion

    estacionMedrano.setServiciosDeEstablecimiento(serviciosRetiro);
    estacionMedrano.setNombre("Estacion Retiro");
    estacionMedrano.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
    estacionMedrano.setEntidad(lineaA);
    estaciones.add(estacionMedrano);




    //Seteo Comunidad

    comunidadVecinosRetiro.setNombre("Vecinos Retiro");
    comunidadSubteLineaB.setNombre("Subte Linea B");
    miembrosComunidadRetiro.add(miembroMatias);
    miembrosComunidadRetiro.add(miembroJuan);
    miembrosComunidadLineaB.add(miembroMatias);



    comunidadSubteLineaB.setMiembros(miembrosComunidadLineaB);
    comunidadVecinosRetiro.setMiembros(miembrosComunidadRetiro);
    interesadosEnEscaleraRetiro.add(comunidadVecinosRetiro);

    comunidadVecinosRetiro.setPrestacionesDeInteres(prestacionesDeInteres);
    comunidadVecinosRetiro.setIncidentesMiembros(incidentesRetiro);

    comunidadesMatias.add(comunidadVecinosRetiro);
    comunidadesMatias.add(comunidadSubteLineaB);
    comunidadesJuan.add(comunidadVecinosRetiro);

    //-----




    //Seteo incidente

    incidenteEscaleraRetiro.setTitulo("Incidente en estacion Retiro");
    incidenteEscaleraRetiro.setDetalle("No funciona escalera mecanica ");
    incidenteEscaleraRetiro.setEstaAbierto(Boolean.TRUE);
    incidenteEscaleraRetiro.setFechaApertura(LocalDateTime.now());
    incidenteEscaleraRetiro.setEstaAbierto(Boolean.TRUE);
    incidenteEscaleraRetiro.setPrestacion(prestacion);
    incidentesRetiro.add(incidenteEscaleraRetiro);
    incidenteEscaleraRetiro.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteEscaleraRetiro.setComunidad(comunidadVecinosRetiro);




    //Seteo Prestacion

    prestacion.setServicio(banioHombre);
    prestacion.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacion.setIncidentes(incidentesRetiro);
    prestacion.setEstablecimiento(estacionMedrano);
    prestacion.setComunidadesInteresadas(interesadosEnEscaleraRetiro);
    prestacionesDeInteres.add(prestacion);




    //Seteo Miembro

    miembroMatias.setUsuario(usuarioMatias);
    miembroMatias.setNombre("Matias Agustin");
    miembroMatias.setApellido("Fernandez");
    miembroMatias.setModoUsuario(ModoUsuario.OBSERVADOR);
    miembroMatias.setNotificadorPreferido(new MailNotifier());
    miembroMatias.setModoNotificacion(new NotificadorEnInstante());
    miembroMatias.setIncidentesSinNotificar(incidentes);
    miembroMatias.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroMatias.setComunidadesQueEsMiembro(comunidadesMatias);


    miembroJuan.setUsuario(usuarioMatias);
    miembroJuan.setNombre("Juan");
    miembroJuan.setApellido("Iwassjuk");
    miembroJuan.setModoUsuario(ModoUsuario.OBSERVADOR);
    miembroJuan.setNotificadorPreferido(new MailNotifier());
    miembroJuan.setModoNotificacion(new NotificadorEnInstante());
    miembroJuan.setIncidentesSinNotificar(incidentes);
    miembroJuan.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroJuan.setComunidadesQueEsMiembro(comunidadesJuan);





    //Persistencia

    em.persist(permisoAbrirIncidente);
    em.persist(rolAdmin);
    em.persist(usuarioMatias);
    em.persist(comunidadVecinosRetiro);
    em.persist(prestacion);
    em.persist(incidenteEscaleraRetiro);
    em.persist(lineaA);
    em.persist(estacionMedrano);
    em.persist(miembroMatias);


    em.persist(permisoAbrirIncidente);
    em.persist(rolAdmin);
    em.persist(usuarioJuan);
    em.persist(prestacion);
    em.persist(incidenteEscaleraRetiro);
    em.persist(lineaA);
    em.persist(estacionMedrano);
    em.persist(miembroJuan);


    em.persist(comunidadSubteLineaB);

    return this;

  }




















}
