package py.edu.uc.lp3exceptions;

/** Regla de munición para consumibles: una granada ya lanzada no se puede volver a usar. */
public class GranadaUsadaException extends ReglaDeJuegoException {

    public GranadaUsadaException(String mensaje) {
        super("GRANADA_USADA", mensaje);
    }
}
