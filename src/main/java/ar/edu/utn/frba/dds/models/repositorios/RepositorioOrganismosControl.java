package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.controllers.OrganismosController;
import ar.edu.utn.frba.dds.models.entidades.entidadescsv.OrganismoDeControl;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioOrganismosControl implements WithSimplePersistenceUnit, ICrudRepository {


  public RepositorioOrganismosControl() {
  }

  @Override
  public List buscarTodos() {
    return entityManager().createQuery("from " + OrganismoDeControl.class.getName()).getResultList();
  }

  @Override
  public Object buscar(Long id) {
    return entityManager().find(OrganismoDeControl.class, id);
  }

  @Override
  public void guardar(Object o) {
    EntityTransaction tx = entityManager().getTransaction();
    if(!tx.isActive())
      tx.begin();

    entityManager().persist(o);
    tx.commit();
  }

  @Override
  public void actualizar(Object o) {
    EntityTransaction tx = entityManager().getTransaction();
    if(!tx.isActive())
      tx.begin();

    entityManager().merge(o);
    tx.commit();
  }

  @Override
  public void eliminar(Object o) {
    EntityTransaction tx = entityManager().getTransaction();
    if(!tx.isActive())
      tx.begin();

    entityManager().remove(o);
    tx.commit();
  }


  public void guardarOrganismos(List<OrganismoDeControl> organismoDeControls) {
    EntityTransaction tx = entityManager().getTransaction();
    if(!tx.isActive())
      tx.begin();
    organismoDeControls.forEach(organismoDeControl -> entityManager().persist(organismoDeControl));
    tx.commit();
  }


}
