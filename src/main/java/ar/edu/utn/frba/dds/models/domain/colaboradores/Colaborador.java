package ar.edu.utn.frba.dds.models.domain.colaboradores;

import ar.edu.utn.frba.dds.helpers.MedioContactoHelper;
import ar.edu.utn.frba.dds.models.db.EntidadPersistente;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.ColocacionHeladeras;
import ar.edu.utn.frba.dds.models.domain.colaboradores.autenticacion.Usuario;
import ar.edu.utn.frba.dds.models.domain.notifications.Contactable;
import ar.edu.utn.frba.dds.models.domain.utils.CanalContacto;
import ar.edu.utn.frba.dds.models.domain.utils.Direccion;
import ar.edu.utn.frba.dds.models.domain.utils.MedioDeContacto;
import ar.edu.utn.frba.dds.models.domain.utils.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Colaborador class permite representar un colaborador.
 */

@Entity
@Table(name = "colaborador",
    uniqueConstraints = @UniqueConstraint(columnNames = {"tipo_documento", "documento"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Colaborador extends EntidadPersistente implements Contactable {

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false, unique = true)
  private Usuario usuario;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_documento")
  private TipoDocumento tipoDocumento;

  @Column(name = "documento", columnDefinition = "varchar(11)")
  private String documento;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "tipo_colaborador_id", referencedColumnName = "id", unique = true)
  private TipoColaborador tipoColaborador;

  @Column(name = "puntosGanados")
  private Float puntosGanados = 0f;

  @OneToMany(mappedBy = "colaborador", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
  private List<ColocacionHeladeras> heladerasColocadas = new ArrayList<>();

  @Embedded
  private Direccion direccion;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
  @JoinColumn(name = "colaborador_id", referencedColumnName = "id")
  private List<MedioDeContacto> medioContacto = new ArrayList<>();

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "fechaNacimiento")
  private LocalDate fechaNacimiento;

  @Column(name = "rubro")
  private String rubro;

  @Column(name = "razonSocial")
  private String razonSocial;

  @Column(name = "tipo_persona_juridica")
  @Enumerated(EnumType.STRING)
  private TipoPersonaJuridica tipoPersonaJuridica;

  @Column(name = "form_completado")
  private Boolean formCompletado;

  public void sumarPuntos(Float puntos) {
    this.puntosGanados += puntos;
  }

  public void restarPuntos(Float puntos) {
    this.puntosGanados -= puntos;
  }

  public void agregarColocacionHeladera(ColocacionHeladeras colocacion) {
    this.heladerasColocadas.add(colocacion);
    colocacion.setColaborador(this);
  }

  public String getNombreYapellido() {
    return String.format("%s %s", this.nombre, this.apellido);
  }

  public void agregarMedioContacto(MedioDeContacto... medio) {
    this.medioContacto.addAll(Arrays.stream(medio).toList());
  }

  public void agregarMedioContacto(List<MedioDeContacto> medios) {
    this.medioContacto.addAll(medios);
  }

  @Override
  public String email() {
    return MedioContactoHelper.getValorContacto(this.medioContacto, CanalContacto.EMAIL);
  }

  @Override
  public String telefonoCompleto() {
    return MedioContactoHelper.getValorContacto(this.medioContacto, CanalContacto.WHATSAPP);
  }

  @Override
  public String telegramId() {
    return MedioContactoHelper.getValorContacto(this.medioContacto, CanalContacto.TELEGRAM);
  }
}

//el colaborador a lo largo del tiempo podria tener distintas tarjetas
//podria tener una lista de tarjetas con el dia que se dio de alta y de baja