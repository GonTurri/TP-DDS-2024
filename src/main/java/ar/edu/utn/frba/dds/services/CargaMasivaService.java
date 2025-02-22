package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.exceptions.CargaArchivoFailedException;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.AltaPersonaVulnerable;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.ColocacionHeladeras;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.DonacionDinero;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.IPuntajeCalculable;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.calculadores.ICalculadorPuntos;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.cargaMasiva.CargaColaboracionCsvReader;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.cargaMasiva.CargadorDeColaboraciones;
import ar.edu.utn.frba.dds.models.domain.emailSending.SendGridMailSender;
import ar.edu.utn.frba.dds.models.domain.excepciones.CsvInvalidoException;
import ar.edu.utn.frba.dds.models.messageFactory.MensajeFilaCsvInvalidaFactory;
import ar.edu.utn.frba.dds.models.repositories.IAltaPersonaVulnerableRepository;
import ar.edu.utn.frba.dds.models.repositories.IColaboradoresRepository;
import ar.edu.utn.frba.dds.models.repositories.IColocacionHeladeraRepository;
import ar.edu.utn.frba.dds.models.repositories.IDonacionDineroRepository;
import ar.edu.utn.frba.dds.models.repositories.IDonacionesViandaRepository;
import ar.edu.utn.frba.dds.models.repositories.IFormasColaboracionRespository;
import ar.edu.utn.frba.dds.models.repositories.IHeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.IRedistribucionesViandaRepository;
import ar.edu.utn.frba.dds.models.repositories.IRolesRepository;
import io.javalin.http.UploadedFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class CargaMasivaService {
  private FileUploadService service;
  private CargaColaboracionCsvReader csvReader;
  private SendGridMailSender mailSender;
  private IColaboradoresRepository colaboradoresRepository;
  private IFormasColaboracionRespository formasColaboracionRespository;
  private ICalculadorPuntos calculadorPuntos;
  private IDonacionesViandaRepository donacionesViandaRepository;
  private IDonacionDineroRepository donacionesDineroRepository;
  private IColocacionHeladeraRepository colocacionHeladeraRepository;
  private IRedistribucionesViandaRepository redistribucionesViandaRepository;
  private IAltaPersonaVulnerableRepository altaPersonaVulnerableRepository;
  private IHeladerasRepository heladerasRepository;
  private IRolesRepository rolesRepository;

  public String subirArchivo(UploadedFile uploadedFile) throws CargaArchivoFailedException {
    try {
      return this.service.uploadFile(uploadedFile);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void cargarColaboraciones(UploadedFile uploadedFile) throws CargaArchivoFailedException {
    String filePath = this.subirArchivo(uploadedFile);
    try {
      CargadorDeColaboraciones cargador = new CargadorDeColaboraciones(
          filePath.substring(1),
          this.csvReader,
          this.mailSender,
          this.colaboradoresRepository,
          this.formasColaboracionRespository,
          this.calculadorPuntos,
          this.rolesRepository
      );

      List<IPuntajeCalculable> colaboraciones = cargador.cargarColaboraciones();

      for (IPuntajeCalculable colab : colaboraciones) {
        this.guardarColaboracion(colab);
      }

    } catch (IOException e) {
      e.printStackTrace();
      throw new CsvInvalidoException(MensajeFilaCsvInvalidaFactory.generarMensaje(e.getMessage()));
    }
  }

  public void guardarColaboracion(IPuntajeCalculable colab) {
    Class<?> claseColab = colab.getClass();
    if (claseColab.equals(DonacionDinero.class)) {
      this.donacionesDineroRepository.guardar((DonacionDinero) colab);
    } else if (claseColab.equals(DonacionVianda.class)) {
      DonacionVianda d = (DonacionVianda) colab;
      d.getVianda().setHeladera(this.heladerasRepository.buscarPorNombre(d.getVianda().getHeladera().getNombre()));
      this.donacionesViandaRepository.guardar(d);
    } else if (claseColab.equals(AltaPersonaVulnerable.class)) {
      this.altaPersonaVulnerableRepository.guardar((AltaPersonaVulnerable) colab);
    } else if (claseColab.equals(ColocacionHeladeras.class)) {
      this.colocacionHeladeraRepository.guardar((ColocacionHeladeras) colab);
    } else if (claseColab.equals(RedistribucionViandas.class)) {
      RedistribucionViandas r = (RedistribucionViandas) colab;
      r.setHeladeraOrigen(this.heladerasRepository.buscarPorNombre(r.getHeladeraOrigen().getNombre()));
      r.setHeladeraDestino(this.heladerasRepository.buscarPorNombre(r.getHeladeraDestino().getNombre()));
      this.redistribucionesViandaRepository.guardar(r);
    }
  }
}
