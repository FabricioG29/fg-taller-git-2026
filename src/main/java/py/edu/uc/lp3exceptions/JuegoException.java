package py.edu.uc.lp3exceptions;

/**
 * Base de la jerarquía de excepciones del dominio CS2.
 * Es no comprobada (RuntimeException): una regla rota no se "maneja" en cada
 * método intermedio; sube hasta el handler HTTP y además hace que Spring
 * deshaga la transacción (@Transactional revierte ante RuntimeException).
 * Cada hija lleva un código estable para que el cliente la distinga en el JSON.
 */
public abstract class JuegoException extends RuntimeException {

    private final String codigo;

    protected JuegoException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
