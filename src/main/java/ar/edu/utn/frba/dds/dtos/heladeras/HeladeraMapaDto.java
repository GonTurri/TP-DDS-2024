package ar.edu.utn.frba.dds.dtos.heladeras;

import ar.edu.utn.frba.dds.models.domain.heladeras.Heladera;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class HeladeraMapaDto {
  private String id;
  private List<Float> coords;
  private String title;
  private boolean disabled;

  public static HeladeraMapaDto fromHeladera(Heladera h) {
    List<Float> coords = new ArrayList<>();
    coords.add(h.getUbicacion().getLatitud());
    coords.add(h.getUbicacion().getLongitud());
    return new HeladeraMapaDto(h.getId(), coords, h.getNombre(), !h.isHeladeraActiva());
  }
}