package ar.edu.utn.frba.dds.models.domain.colaboraciones.utils;

import ar.edu.utn.frba.dds.models.db.EntidadPersistente;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.OfertaProducto;
import ar.edu.utn.frba.dds.models.domain.colaboradores.Colaborador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * CanjeProducto class representa una colaboracion de un colaborador.
 * Consiste en canjear un producto.
 */
@Entity
@Table(name = "canje_producto")
@Getter
@Setter
@NoArgsConstructor
public class CanjeProducto extends EntidadPersistente {
  @ManyToOne
  @JoinColumn(name = "colaborador_id", referencedColumnName = "id")
  private Colaborador comprador;

  @ManyToOne
  @JoinColumn(name = "oferta_producto_id", referencedColumnName = "id")
  private OfertaProducto ofertaCanjeada;

  @Column(name = "fecha_canje", columnDefinition = "DATETIME")
  private LocalDateTime fechaCanje;

  @Column(name = "puntos_gastados")
  private Float puntosGastados;

  /**
   * Constructor con parametros.
   */
  public CanjeProducto(Colaborador comprador,
                       OfertaProducto ofertaCanjeada,
                       LocalDateTime fechaCanje, Float puntosGastados) {
    this.comprador = comprador;
    this.ofertaCanjeada = ofertaCanjeada;
    this.fechaCanje = fechaCanje;
    this.puntosGastados = puntosGastados;
  }
}
