package ar.edu.utn.frba.dds.dtos.personas;

import ar.edu.utn.frba.dds.dtos.DireccionDto;
import ar.edu.utn.frba.dds.models.domain.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.models.domain.utils.TipoDocumentoMapper;
import ar.edu.utn.frba.dds.serviceLocator.ServiceLocator;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ColaboradorPerfilDto {
  private String id;
  private String nombre;
  private String apellido;
  private String email;
  private String documento;
  private String tipoDocumento;
  private String fechaNacimiento;
  private DireccionDto direccionDto;
  private String rubro;
  private String razonSocial;
  private String tipoPersonaJuridica;
  private Float puntosGanados;
  private Boolean formCompletado;
  private List<MedioContactoDto> mediosContacto;
  private List<FormaColaboracionOutputDto> formaColaboracionDtos;
  private List<FormaColaboracionDto> formaColaboracionInput;

  public static ColaboradorPerfilDto of(Context context) {
    if (context.formParam("nombre") != null) {
      return ColaboradorPerfilDto
          .builder()
          .id(context.pathParam("id"))
          .nombre(context.formParam("nombre"))
          .apellido(context.formParam("apellido"))
          .email(context.formParam("email"))
          .documento(context.formParam("documento"))
          .tipoDocumento(context.formParam("tipoDocumento"))
          .fechaNacimiento((context.formParam("fechaNacimiento") != null && !context.formParam("fechaNacimiento").isBlank()) ? context.formParam("fechaNacimiento") : null)
          .direccionDto(DireccionDto.of(context))
          .formaColaboracionInput(FormaColaboracionDto.of(context))
          .build();
    } else {
      return ColaboradorPerfilDto
          .builder()
          .id(context.pathParam("id"))
          .email(context.formParam("email"))
          .direccionDto(DireccionDto.of(context))
          .rubro(context.formParam("rubro"))
          .razonSocial(context.formParam("razonSocial"))
          .tipoPersonaJuridica(context.formParam("tipoPersonaJuridica"))
          .formaColaboracionInput(FormaColaboracionDto.of(context))
          .build();
    }
  }

  public static ColaboradorPerfilDto fromColaborador(Colaborador colaborador) {
    return ColaboradorPerfilDto
        .builder()
        .id(colaborador.getId())
        .nombre(colaborador.getNombre())
        .apellido(colaborador.getApellido())
        .email(colaborador.getUsuario().getEmail())
        .documento(colaborador.getDocumento())
        .tipoDocumento(colaborador.getTipoDocumento() != null ? ServiceLocator.get(TipoDocumentoMapper.class).mapearAstring(colaborador.getTipoDocumento()) : null)
        .fechaNacimiento(String.valueOf(colaborador.getFechaNacimiento()))
        .direccionDto(DireccionDto.fromDireccion(colaborador.getDireccion()))
        .rubro(colaborador.getRubro())
        .razonSocial(colaborador.getRazonSocial())
        .tipoPersonaJuridica(colaborador.getTipoPersonaJuridica() != null ? colaborador.getTipoPersonaJuridica().name() : null)
        .puntosGanados(colaborador.getPuntosGanados())
        .formCompletado(colaborador.getFormCompletado())
        .formaColaboracionDtos(colaborador.getTipoColaborador().getFormasPosiblesColaboracion().stream().map(FormaColaboracionOutputDto::fromForma).toList())
        .build();
  }
}
