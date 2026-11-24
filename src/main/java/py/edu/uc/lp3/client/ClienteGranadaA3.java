package py.edu.uc.lp3.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import py.edu.uc.lp3.domain.Equipo;

/**
 * A3 — Diana Aquino (CS2). Regla consultada, según su OpenAPI:
 * POST /api/armas/granadas/simular?equipoAtacante=&equipoObjetivo=&distancia= → 200 si el
 * lanzamiento es legal; 422 FuegoAmigoException si los dos son del mismo equipo;
 * 400 si la distancia es negativa o mayor a 35 m.
 * Los nombres de equipo de su API son distintos a los míos: se traducen por propiedades.
 */
@Component
public class ClienteGranadaA3 extends ClienteCompanero {

    private final String ruta;
    private final String nombreTerroristas;
    private final String nombreAntiterroristas;

    public ClienteGranadaA3(@Value("${companeros.a3.nombre}") String nombre,
                            @Value("${companeros.a3.base-url}") String baseUrl,
                            @Value("${companeros.a3.ruta}") String ruta,
                            @Value("${companeros.a3.equipo-terroristas:Terrorista}") String nombreTerroristas,
                            @Value("${companeros.a3.equipo-antiterroristas:Counter-Terrorista}") String nombreAntiterroristas,
                            @Value("${companeros.timeout-ms:3000}") int timeoutMs) {
        super("A3", nombre, baseUrl, timeoutMs);
        this.ruta = ruta;
        this.nombreTerroristas = nombreTerroristas;
        this.nombreAntiterroristas = nombreAntiterroristas;
    }

    /** Pregunta si el lanzamiento es legal. Devuelve su respuesta tal cual (para guardarla en la jugada). */
    public String validarLanzamiento(Equipo atacante, Equipo objetivo, double distancia) {
        String equipoAtacante = traducir(atacante);
        String equipoObjetivo = traducir(objetivo);
        String rutaConParametros = ruta + "?equipoAtacante=" + equipoAtacante + "&equipoObjetivo=" + equipoObjetivo
                + "&distancia=" + distancia;
        String respuesta = consultar(rutaConParametros, () -> http.post()
                .uri(u -> u.path(ruta)
                        .queryParam("equipoAtacante", equipoAtacante)
                        .queryParam("equipoObjetivo", equipoObjetivo)
                        .queryParam("distancia", distancia)
                        .build())
                .retrieve()
                .body(String.class));
        return recortar(respuesta);
    }

    private String traducir(Equipo equipo) {
        return equipo == Equipo.ANTITERRORISTAS ? nombreAntiterroristas : nombreTerroristas;
    }
}
