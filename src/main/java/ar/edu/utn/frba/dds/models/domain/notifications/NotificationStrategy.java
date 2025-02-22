package ar.edu.utn.frba.dds.models.domain.notifications;

/**
 * Estrategia para notificar segun el medio de contacto elegido
 */
public interface NotificationStrategy {

  void notificar(Contactable contactable, String asunto, String message);

}