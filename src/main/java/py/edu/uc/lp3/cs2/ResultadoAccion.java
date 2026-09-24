package py.edu.uc.lp3.cs2;

/**
 * Lo que un arma informa después de usarse. Es inmutable (record):
 * sirve para responder JSON sin exponer el estado interno del arma.
 */
public record ResultadoAccion(String arma, String tipo, String accion, int danio, String detalle) {
}
