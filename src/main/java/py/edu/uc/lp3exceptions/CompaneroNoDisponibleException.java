package py.edu.uc.lp3exceptions;

/**
 * A2 o A3 no se pudo consultar: está apagado, tardó demasiado, respondió 5xx,
 * la ruta configurada no existe (404/405) o la respuesta no trae lo que dice su
 * OpenAPI. No es un rechazo de su regla: es 503 y tampoco se guarda nada.
 */
public class CompaneroNoDisponibleException extends CompaneroException {

    public CompaneroNoDisponibleException(String rol, String companero, String urlConsultada,
                                          Integer statusCompanero, String respuestaCompanero, String motivo) {
        super("COMPANERO_NO_DISPONIBLE_" + rol, rol + " (" + companero + ") no está disponible: " + motivo
                + ". No se guardó nada", rol, companero, urlConsultada, statusCompanero, respuestaCompanero);
    }
}
