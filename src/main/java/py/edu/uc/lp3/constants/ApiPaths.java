package py.edu.uc.lp3.constants;

/** Centraliza las rutas de la API REST para no repetir strings en los controllers. */
public final class ApiPaths {

    private ApiPaths() {
    }

    public static final String INDEX = "/";
    public static final String ARMAS = "/api/armas";
    public static final String PISTOLA = "/pistola";
    public static final String COMPORTAMIENTOS = "/comportamientos";
    public static final String TIENDA = "/tienda";
}
