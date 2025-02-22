package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dtos.tecnicos.VisitaTecnicoDto;
import ar.edu.utn.frba.dds.services.FileUploadService;
import ar.edu.utn.frba.dds.services.IncidentesService;
import ar.edu.utn.frba.dds.services.TecnicosService;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import lombok.AllArgsConstructor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class VisitasTecnicoController implements ICrudViewsHandler {
  private TecnicosService tecnicosService;
  private IncidentesService incidentesService;
  private FileUploadService fileUploadService;

  @Override
  public void index(Context context) {
    Map<String, Object> model = new HashMap<>();
    model.put("visitas", this.tecnicosService.obtenerTodasLasVisitas());
    context.render("app/admin/listado-visitas-tecnicos.hbs", model);
  }

  @Override
  public void show(Context context) {
    Map<String, Object> model = new HashMap<>();
    model.put("visita", this.tecnicosService.obtenerVisita(context.pathParam("id")));
    context.render("app/admin/visita-tecnica.hbs", model);
  }

  @Override
  public void create(Context context) {
    Map<String, Object> model = new HashMap<>();
    model.put("message", context.queryParam("message"));
    model.put("tecnicos", this.tecnicosService.obtenerTodos());
    model.put("incidentes", this.incidentesService.obtenerIncidentes());
    context.render("app/admin/alta-visita-tecnico.hbs", model);
  }

  @Override
  public void save(Context context) {
    VisitaTecnicoDto dto = VisitaTecnicoDto.of(context);
    UploadedFile uploadedFile = context.uploadedFile("file");
    try {
      if (uploadedFile != null) {
        String result = this.fileUploadService.uploadFile(uploadedFile);
        dto.setUrlFoto(result);
      }
      this.tecnicosService.crearVisita(dto);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Map<String, Object> model = new HashMap<>();
    model.put("message", "La visita se registró con éxito");
    context.status(201);
    context.render("app/success.hbs", model);
  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }
}
