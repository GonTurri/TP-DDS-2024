package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.domain.colaboraciones.OfertaProducto;
import java.util.List;
import java.util.Optional;

public interface IOfertaProductoRepository {
  Optional<OfertaProducto> buscar(String id);

  List<OfertaProducto> buscarTodos();

  void guardar(OfertaProducto ofertaProducto);

  void actualizar(OfertaProducto ofertaProducto);

  void eliminar(OfertaProducto ofertaProducto);
}
