package py.edu.uc.lp3.domain;

/**
 * Protocolo del dominio: cualquier cosa del juego que se puede inspeccionar
 * desde el HUD (armas y jugadores, de familias distintas) responde con un
 * resumen de su estado, sin exponer sus campos.
 */
public interface Inspeccionable {

    /** Resumen legible del estado actual del objeto. */
    String inspeccionar();
}
