package py.edu.uc.lp3exceptions;

/** A2 o A3 respondió 4xx: su regla no acepta la jugada. Código RECHAZO_A2 o RECHAZO_A3 (409). */
public class RechazoDeCompaneroException extends CompaneroException {

    public RechazoDeCompaneroException(String rol, String companero, String urlConsultada,
                                       int statusCompanero, String respuestaCompanero) {
        super("RECHAZO_" + rol, rol + " (" + companero + ") rechazó la jugada con HTTP " + statusCompanero
                        + ": no se guardó nada", rol, companero, urlConsultada, statusCompanero, respuestaCompanero);
    }
}
