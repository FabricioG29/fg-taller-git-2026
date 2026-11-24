package py.edu.uc.lp3exceptions;

/**
 * Base de los errores que vienen de otro servicio del grupo (A2 o A3).
 * Guarda a quién se consultó y qué contestó, para atribuir el problema:
 * el cliente HTTP sabe si el rechazo vino de A2 o de A3.
 */
public abstract class CompaneroException extends JuegoException {

    private final String rol;
    private final String companero;
    private final String urlConsultada;
    private final Integer statusCompanero;
    private final String respuestaCompanero;

    protected CompaneroException(String codigo, String mensaje, String rol, String companero,
                                 String urlConsultada, Integer statusCompanero, String respuestaCompanero) {
        super(codigo, mensaje);
        this.rol = rol;
        this.companero = companero;
        this.urlConsultada = urlConsultada;
        this.statusCompanero = statusCompanero;
        this.respuestaCompanero = respuestaCompanero;
    }

    public String getRol() {
        return rol;
    }

    public String getCompanero() {
        return companero;
    }

    public String getUrlConsultada() {
        return urlConsultada;
    }

    public Integer getStatusCompanero() {
        return statusCompanero;
    }

    public String getRespuestaCompanero() {
        return respuestaCompanero;
    }
}
