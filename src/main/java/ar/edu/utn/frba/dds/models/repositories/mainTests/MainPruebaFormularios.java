package ar.edu.utn.frba.dds.models.repositories.mainTests;

import ar.edu.utn.frba.dds.models.domain.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.models.domain.colaboradores.autenticacion.Usuario;
import ar.edu.utn.frba.dds.models.domain.colaboradores.form.Campo;
import ar.edu.utn.frba.dds.models.domain.colaboradores.form.Formulario;
import ar.edu.utn.frba.dds.models.domain.colaboradores.form.Opcion;
import ar.edu.utn.frba.dds.models.domain.colaboradores.form.RespuestaACampo;
import ar.edu.utn.frba.dds.models.domain.colaboradores.form.RespuestaFormulario;
import ar.edu.utn.frba.dds.models.domain.colaboradores.form.TipoCampo;
import ar.edu.utn.frba.dds.models.repositories.IColaboradoresRepository;
import ar.edu.utn.frba.dds.models.repositories.IFormularioRepository;
import ar.edu.utn.frba.dds.models.repositories.IRespuestasFormularioRepository;
import ar.edu.utn.frba.dds.serviceLocator.ServiceLocator;
import java.util.Optional;

public class MainPruebaFormularios {
  public static void main(String[] args) {
    Colaborador c = new Colaborador();
    c.setNombre("horge");
    c.setApellido("fdfdf");
    c.setUsuario(new Usuario("fdknfdknf", "dkfndknfkdnafa"));
    ServiceLocator.get(IColaboradoresRepository.class).guardar(c);
    IFormularioRepository formularioRepository = ServiceLocator.get(IFormularioRepository.class);
    IRespuestasFormularioRepository respuestasFormularioRepository = ServiceLocator.get(IRespuestasFormularioRepository.class);
    Formulario nuevo = new Formulario();
    nuevo.agregarCampos(new Campo(TipoCampo.LIBRE, "quien sos", true),
        new Campo(TipoCampo.LIBRE, "te gusta el helado", false));
    Campo compuesto = new Campo(TipoCampo.MULTIPLE_CHOICE, "vas a aprobar dds?", true);
    compuesto.agregarOpciones(new Opcion("si"), new Opcion("no"));
    nuevo.agregarCampos(compuesto);
    formularioRepository.guardar(nuevo);
    RespuestaFormulario unaRespuesta = new RespuestaFormulario();
    unaRespuesta.setColaborador(c);
    RespuestaACampo unaOpcion = new RespuestaACampo();
    unaRespuesta.agregarRespuestasACampo(unaOpcion);
    Optional<Formulario> hidratado = formularioRepository.buscar(nuevo.getId());
    if (hidratado.isEmpty()) {
      System.exit(1);
    }

    Campo campo3 = hidratado.get().getCampos().get(hidratado.get().getCampos().size() - 1);
    unaOpcion.setCampo(campo3);
    unaOpcion.agregarOpcionesElegidas(campo3.getOpciones().get(0));

    RespuestaFormulario otraRespuesta = new RespuestaFormulario();
    otraRespuesta.setColaborador(c);
    RespuestaACampo laOtraOpcion = new RespuestaACampo();

    otraRespuesta.agregarRespuestasACampo(laOtraOpcion);
    laOtraOpcion.setCampo(campo3);
    laOtraOpcion.agregarOpcionesElegidas(campo3.getOpciones().get(1));

    unaRespuesta.setFormulario(hidratado.get());
    otraRespuesta.setFormulario(hidratado.get());

    respuestasFormularioRepository.guardar(unaRespuesta);
    respuestasFormularioRepository.guardar(otraRespuesta);


  }
}