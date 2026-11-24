package py.edu.uc.lp3.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Dobles de prueba de A2 y A3: servidores HTTP reales (del JDK) que responden lo que
 * dice el OpenAPI de cada compañero. No hay código de ellos: solo su contrato.
 *  A2 GET  /api/simulacion/explosion   → 400 si distancia < 0; si no, 200 con el daño atenuado (radio 8 m).
 *  A3 POST /api/armas/granadas/simular → 422 si los equipos son iguales; 400 si distancia < 0 o > 35; si no, 200.
 */
public final class CompanerosFalsos implements AutoCloseable {

    private final HttpServer a2;
    private final HttpServer a3;

    public CompanerosFalsos() throws IOException {
        a2 = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        a2.createContext("/api/simulacion/explosion", this::explosion);
        a2.start();
        a3 = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        a3.createContext("/api/armas/granadas/simular", this::granada);
        a3.start();
    }

    public String urlA2() {
        return "http://localhost:" + a2.getAddress().getPort();
    }

    public String urlA3() {
        return "http://localhost:" + a3.getAddress().getPort();
    }

    private void explosion(HttpExchange ex) throws IOException {
        double distancia = Double.parseDouble(parametros(ex.getRequestURI()).getOrDefault("distancia", "2.5"));
        if (distancia < 0) {
            responder(ex, 400, "{\"status\":400,\"error\":\"ParametroInvalidoException\",\"message\":\"La distancia no puede ser negativa\"}");
            return;
        }
        double radio = 8.0;
        boolean enRadio = distancia <= radio;
        double atenuado = enRadio ? Math.round(98.0 * (1 - distancia / radio) * 100) / 100.0 : 0;
        responder(ex, 200, "{\"armaUtilizada\":\"Granada HE\",\"enRadioAlcance\":" + enRadio
                + ",\"distanciaMetros\":" + distancia + ",\"radioExplosion\":8.0,\"danioNominal\":98.0"
                + ",\"danioAtenuadoDistancia\":" + atenuado + ",\"detalleRegla\":\"doble de prueba\"}");
    }

    private void granada(HttpExchange ex) throws IOException {
        Map<String, String> p = parametros(ex.getRequestURI());
        double distancia = Double.parseDouble(p.getOrDefault("distancia", "0"));
        if (p.get("equipoAtacante").equals(p.get("equipoObjetivo"))) {
            responder(ex, 422, "{\"status\":422,\"error\":\"FuegoAmigoException\",\"mensaje\":\"Fuego amigo\"}");
        } else if (distancia < 0 || distancia > 35) {
            responder(ex, 400, "{\"status\":400,\"error\":\"Bad Request\",\"mensaje\":\"Distancia fuera de rango\"}");
        } else {
            responder(ex, 200, "{\"resultado\":\"Explosión válida\",\"distancia\":" + distancia + "}");
        }
    }

    private static Map<String, String> parametros(URI uri) {
        Map<String, String> mapa = new HashMap<>();
        String query = uri.getRawQuery();
        if (query != null) {
            for (String par : query.split("&")) {
                String[] kv = par.split("=", 2);
                mapa.put(kv[0], kv.length > 1 ? java.net.URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "");
            }
        }
        return mapa;
    }

    private static void responder(HttpExchange ex, int status, String json) throws IOException {
        byte[] cuerpo = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(status, cuerpo.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(cuerpo);
        }
    }

    @Override
    public void close() {
        a2.stop(0);
        a3.stop(0);
    }
}
