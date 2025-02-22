package ar.edu.utn.frba.dds.dtos.heladeras;

import ar.edu.utn.frba.dds.dtos.DireccionDto;
import ar.edu.utn.frba.dds.dtos.UbicacionDto;
import io.javalin.http.Context;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HeladeraInputDto {
  private String nombre;
  private String idModelo;
  private Integer capacidad;
  private String fecha;
  private UbicacionDto ubicacion;
  private DireccionDto direccion;
  private Boolean sensorTemp;
  private Boolean sensorMov;
  private String idColaborador;

  public static HeladeraInputDto of(Context context) {
    return HeladeraInputDto.builder().nombre(sanitizarNombreHeladera(context.formParam("nombre")))
        .idModelo(context.formParam("modelo"))
        .capacidad(Integer.valueOf(context.formParam("capacidad")))
        .fecha(context.formParam("fecha"))
        .ubicacion(new UbicacionDto(Float.valueOf(context.formParam("latitudElegida")), Float.valueOf(context.formParam("longitudElegida"))))
        .direccion(DireccionDto.builder().calle(context.formParam("calle")).altura(Integer.valueOf(context.formParam("numero")))
            .piso((context.formParam("piso") != null && !context.formParam("piso").isBlank()) ? Integer.valueOf(context.formParam("piso")) : null)
            .codigoPostal(context.formParam("codPostal"))
            .build())
        .idColaborador(context.sessionAttribute("idColaborador"))
        .sensorMov(context.formParam("sensorMov") != null && !context.formParam("sensorMov").isBlank())
        .sensorTemp(context.formParam("sensorTemp") != null && !context.formParam("sensorTemp").isBlank())
        .build();
  }

  public boolean estanCamposLlenos() {
    return this.nombre != null && this.capacidad != null && this.fecha != null && this.idModelo != null && this.direccion != null
        && this.ubicacion != null;
  }

  private static String sanitizarNombreHeladera(String nombre) {
    return nombre.trim().replaceAll("\\s+", "-");
  }

}


