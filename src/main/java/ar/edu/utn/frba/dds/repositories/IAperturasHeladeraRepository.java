package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.domain.heladeras.AperturaHeladera;
import java.util.List;
import java.util.Optional;

public interface IAperturasHeladeraRepository {
  Optional<AperturaHeladera> buscar(int id);

  List<AperturaHeladera> buscarTodos();

  void guardar(AperturaHeladera aperturaHeladera);

  void actualizar(AperturaHeladera aperturaHeladera);

  void eliminar(AperturaHeladera aperturaHeladera);
}