package py.edu.uc.lp3.constants;

/** Centraliza las rutas de la API REST para no repetir strings en los controllers. */
public final class ApiPaths {

    private ApiPaths() {
    }

    public static final String INDEX = "/";
    public static final String ARMAS = "/api/armas";
    public static final String JUGADORES = "/api/jugadores";
    public static final String INSPECCION = "/api/inspeccion";
    public static final String SIMULACION_DISPAROS = "/api/simulaciones/disparos";
    public static final String DISPAROS = "/api/disparos";
    public static final String RECARGA = "/recarga";

    /** Solo números: así /api/armas/pistola o /api/armas/tienda no se confunden con un id. */
    public static final String ID = "/{id:\\d+}";

    // Endpoints de POO-06 (se mantienen)
    public static final String PISTOLA = "/pistola";
    public static final String COMPORTAMIENTOS = "/comportamientos";
    public static final String TIENDA = "/tienda";
}
