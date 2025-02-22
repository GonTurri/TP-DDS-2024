package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.models.domain.heladeras.AperturaHeladera;
import ar.edu.utn.frba.dds.models.domain.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.repositories.IAperturasHeladeraRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.Optional;

public class AperturasHeladeraRepository implements IAperturasHeladeraRepository, WithSimplePersistenceUnit {

  @Override
  public Optional<AperturaHeladera> buscar(String id) {
    return Optional.ofNullable(entityManager().find(AperturaHeladera.class, id));
  }

  @Override
  public List<AperturaHeladera> buscarTodos() {
    return entityManager().createQuery("from AperturaHeladera where activo=:activo", AperturaHeladera.class).
        setParameter("activo", true)
        .getResultList();
  }

  @Override
  public List<AperturaHeladera> buscarPorHeladera(Heladera Heladera) {
    return entityManager().createQuery("from AperturaHeladera where activo=:activo and heladera=:heladera", AperturaHeladera.class)
        .setParameter("activo", true)
        .setParameter("heladera", Heladera)
        .getResultList();
  }

  @Override
  public void guardar(AperturaHeladera aperturaHeladera) {
    withTransaction(() -> entityManager().persist(aperturaHeladera));
  }

  @Override
  public void actualizar(AperturaHeladera aperturaHeladera) {
    withTransaction(() -> entityManager().merge(aperturaHeladera));
  }

  @Override
  public void eliminar(AperturaHeladera aperturaHeladera) {
    withTransaction(() -> {
      aperturaHeladera.borrarLogico();
      entityManager().merge(aperturaHeladera);
    });
  }
}
