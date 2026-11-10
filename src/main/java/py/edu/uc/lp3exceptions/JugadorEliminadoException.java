package py.edu.uc.lp3exceptions;

/** Regla de daño: un jugador con vida 0 no puede disparar ni recibir más daño. */
public class JugadorEliminadoException extends ReglaDeJuegoException {

    public JugadorEliminadoException(String mensaje) {
        super("JUGADOR_ELIMINADO", mensaje);
    }
}
