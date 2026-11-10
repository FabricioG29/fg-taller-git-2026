package py.edu.uc.lp3exceptions;

/** Un constructor o un mensaje recibió un valor que dejaría al objeto en un estado imposible. */
public class ValorInvalidoException extends JuegoException {

    public ValorInvalidoException(String mensaje) {
        super("VALOR_INVALIDO", mensaje);
    }
}
