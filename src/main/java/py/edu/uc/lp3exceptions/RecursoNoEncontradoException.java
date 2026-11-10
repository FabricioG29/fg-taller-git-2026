package py.edu.uc.lp3exceptions;

/** Se pidió un arma, jugador o disparo por un id que no existe en la base. */
public class RecursoNoEncontradoException extends JuegoException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super("RECURSO_NO_ENCONTRADO", recurso + " con id " + id + " no existe");
    }
}
