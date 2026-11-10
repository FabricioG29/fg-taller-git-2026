package py.edu.uc.lp3exceptions;

/**
 * Los datos son válidos, pero el estado actual del juego no permite la acción.
 * Agrupa las reglas de la simulación (munición y daño).
 */
public abstract class ReglaDeJuegoException extends JuegoException {

    protected ReglaDeJuegoException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
