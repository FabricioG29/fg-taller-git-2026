package py.edu.uc.lp3exceptions;

/** Regla de munición: se intentó disparar con el cargador vacío. */
public class SinMunicionException extends ReglaDeJuegoException {

    public SinMunicionException(String mensaje) {
        super("SIN_MUNICION", mensaje);
    }
}
