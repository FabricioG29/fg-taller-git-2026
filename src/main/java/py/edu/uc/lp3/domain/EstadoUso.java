package py.edu.uc.lp3.domain;

/**
 * Lo que un arma informa sobre si se puede usar ahora. Cada hija responde a su
 * manera (sobreescritura de Arma.estadoDeUso()); quien decide qué hacer con ese
 * estado es la capa service.
 */
public enum EstadoUso {
    LISTA,
    SIN_MUNICION,
    USADA
}
