package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioMiembros implements WithSimplePersistenceUnit, ICrudRepository{


  public RepositorioMiembros(){}


  @Override
  public List buscarTodos() {
    return entityManager().createQuery("from " + Miembro.class.getName()).getResultList();
  }


  public List<Miembro> buscarPorIdUsuario(long id) {
    List<Miembro> todosLosMiembros = this.buscarTodos();
    return todosLosMiembros.stream().filter(m -> m.getUsuarioId()==id).collect(Collectors.toList());
  }

  @Override
  public Object buscar(Long id) {
    return entityManager().find(Miembro.class, id);
  }

  @Override
  public void guardar(Object o) {
    EntityTransaction tx = entityManager().getTransaction();
    if (!tx.isActive())
      tx.begin();

    entityManager().persist(o);
    tx.commit();
  }

  @Override
  public void actualizar(Object o) {
    EntityTransaction tx = entityManager().getTransaction();
    if (!tx.isActive())
      tx.begin();

    entityManager().merge(o);
    tx.commit();
  }

  @Override
  public void eliminar(Object o) {
    EntityTransaction tx = entityManager().getTransaction();
    if (!tx.isActive())
      tx.begin();

    entityManager().remove(o);
    tx.commit();
  }

}
