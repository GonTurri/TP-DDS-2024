package ar.edu.utn.frba.dds.models.domain.colaboraciones;

import ar.edu.utn.frba.dds.dtos.colaboraciones.RedistribucionViandaDto;
import ar.edu.utn.frba.dds.exceptions.HeladeraLlenaException;
import ar.edu.utn.frba.dds.exceptions.HeladeraVaciaException;
import ar.edu.utn.frba.dds.models.db.EntidadPersistente;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.utils.MotivoRedistribucionVianda;
import ar.edu.utn.frba.dds.models.domain.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.models.domain.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.messageFactory.MensajeHeladeraLLenaFactory;
import ar.edu.utn.frba.dds.models.messageFactory.MensajeHeladeraVaciaFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;


/**
 * RedistribucionViandas class representa una colaboracion de un colaborador.
 * Representa el movimiento de una vianda entre heladeras.
 */

@Entity
@Table(name = "redistribucion_viandas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RedistribucionViandas extends EntidadPersistente implements IPuntajeCalculable {
  @ManyToOne
  @JoinColumn(name = "colaborador_id", referencedColumnName = "id", nullable = false)
  private Colaborador colaborador;

  @Column(name = "fecha_redistribucion", columnDefinition = "DATE", nullable = false)
  private LocalDate fecha;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "heladera_origen", referencedColumnName = "id", nullable = false)
  private Heladera heladeraOrigen;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "heladera_destino", referencedColumnName = "id", nullable = false)
  private Heladera heladeraDestino;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "motivo_id", referencedColumnName = "id")
  private MotivoRedistribucionVianda motivo;

  @Column(name = "cantidad")
  private Integer cantidad;

  public void redistribuirEnOrigen() {
    this.heladeraOrigen.quitarVianda(this.cantidad);
  }

  public void redistribuirEnDestino() {
    this.heladeraDestino.agregarVianda(this.cantidad);
  }

  public void validarCantidades(RedistribucionViandaDto dto) {
    if (this.cantidad > this.heladeraOrigen.getViandas())
      throw new HeladeraVaciaException(MensajeHeladeraVaciaFactory.generarMensaje(this.heladeraOrigen.getNombre()), dto);
    if (this.getCantidad() > this.heladeraDestino.getCuposLibresViandas())
      throw new HeladeraLlenaException(MensajeHeladeraLLenaFactory.generarMensaje(this.heladeraDestino.getNombre()), dto);
  }

  @Override
  public Float calcularPuntaje() {
    return (float) this.cantidad;
  }
}