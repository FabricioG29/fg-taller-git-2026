package py.edu.uc.lp3.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A2 — Matias Enciso (CS2). Regla consultada, según su OpenAPI:
 * GET /api/simulacion/explosion?distancia=&conBlindaje=&fuegoAmigo= → 200 con el daño
 * atenuado por la distancia al radio de la explosión; 400 si la distancia es negativa.
 * Es de solo lectura: no cambia su H2.
 */
@Component
public class ClienteExplosionA2 extends ClienteCompanero {

    private final String ruta;

    public ClienteExplosionA2(@Value("${companeros.a2.nombre}") String nombre,
                              @Value("${companeros.a2.base-url}") String baseUrl,
                              @Value("${companeros.a2.ruta}") String ruta,
                              @Value("${companeros.timeout-ms:3000}") int timeoutMs) {
        super("A2", nombre, baseUrl, timeoutMs);
        this.ruta = ruta;
    }

    /** Pregunta cuánto daño de la explosión llega a esa distancia. */
    public ExplosionA2 medirExplosion(double distancia, boolean conBlindaje, boolean fuegoAmigo) {
        String rutaConParametros = ruta + "?distancia=" + distancia + "&conBlindaje=" + conBlindaje
                + "&fuegoAmigo=" + fuegoAmigo;
        ExplosionA2 explosion = consultar(rutaConParametros, () -> http.get()
                .uri(u -> u.path(ruta)
                        .queryParam("distancia", distancia)
                        .queryParam("conBlindaje", conBlindaje)
                        .queryParam("fuegoAmigo", fuegoAmigo)
                        .build())
                .retrieve()
                .body(ExplosionA2.class));
        if (explosion == null || !explosion.completa()) {
            throw respuestaInesperada(rutaConParametros, "faltan danioNominal o danioAtenuadoDistancia");
        }
        return explosion;
    }
}
