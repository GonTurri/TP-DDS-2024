package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dtos.usuarios.LoginDto;
import ar.edu.utn.frba.dds.dtos.usuarios.UsuarioDto;
import ar.edu.utn.frba.dds.dtos.usuarios.UsuarioNavbarDto;
import ar.edu.utn.frba.dds.exceptions.FormularioNoCompletadoException;
import ar.edu.utn.frba.dds.exceptions.LoginFailedException;
import ar.edu.utn.frba.dds.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.services.UsuarioService;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import lombok.AllArgsConstructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class LoginController implements ICrudViewsHandler {
  private UsuarioService usuarioService;

  public void handleLogin(Context ctx) {
    UsuarioDto usuario = UsuarioDto.of(ctx);
    try {
      LoginDto dto = this.usuarioService.obtenerUsuario(usuario);
      ctx.sessionAttribute("idUsuario", dto.getIdUsuario());
      UsuarioNavbarDto usuarioNavbarDto;
      if (dto.getIdColaborador() != null) {
        ctx.sessionAttribute("idColaborador", dto.getIdColaborador());
        usuarioNavbarDto = usuarioService.getUsuarioNavbar(usuarioService.obtenerUsuario(dto.getIdUsuario()), dto.getIdColaborador());
      } else {
        usuarioNavbarDto = usuarioService.getUsuarioNavbar(usuarioService.obtenerUsuario(dto.getIdUsuario()));
      }

      ctx.sessionAttribute("username", usuarioNavbarDto.getNombre());
      ctx.sessionAttribute("email", usuarioNavbarDto.getEmail());
      ctx.sessionAttribute("permisoTecnico", usuarioNavbarDto.getPermisoTecnico());
      ctx.sessionAttribute("permisoFormulario", usuarioNavbarDto.getPermisoFormulario());
      ctx.sessionAttribute("permisoModeloHeladera", usuarioNavbarDto.getPermisoModeloHeladera());
      ctx.sessionAttribute("permisoTarjeta", usuarioNavbarDto.getPermisoCodTarjeta());
      ctx.sessionAttribute("permisoCsv", usuarioNavbarDto.getPermisoCsv());
      ctx.sessionAttribute("admin", usuarioNavbarDto.esAdmin());

      if (dto.getFormCompletado() != null && !dto.getFormCompletado()) {
        throw new FormularioNoCompletadoException(dto.getIdColaborador());
      }

      String previousUrl = ctx.sessionAttribute("previousUrl");
      ServiceLocator.get(StepMeterRegistry.class).counter("Logins", "status", "ok").increment();
      ctx.redirect(Objects.requireNonNullElse(previousUrl, "/"));
    } catch (LoginFailedException e) {
      Map<String, String> model = new HashMap<>();
      model.put("message", "No existe un usuario con ese email o contraseña. Inténtelo nuevamente.");
      ServiceLocator.get(StepMeterRegistry.class).counter("Logins", "status", "failed").increment();
      ctx.render("auth/login/inicio-sesion.hbs", model);
    }
  }

  @Override
  public void index(Context context) {
    context.render("auth/login/inicio-sesion.hbs");
  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {

  }

  @Override
  public void save(Context context) {

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
