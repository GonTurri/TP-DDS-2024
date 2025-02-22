package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.domain.heladeras.SensorTemperatura;
import java.util.List;
import java.util.Optional;

public interface ISensorTemperaturaRepository {
  Optional<SensorTemperatura> buscar(String id);

  List<SensorTemperatura> buscarTodos();

  void guardar(SensorTemperatura sensorTemperatura);

  void actualizar(SensorTemperatura sensorTemperatura);

  void eliminar(SensorTemperatura sensorTemperatura);
}