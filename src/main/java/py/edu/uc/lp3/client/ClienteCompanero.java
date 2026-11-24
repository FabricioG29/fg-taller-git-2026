package py.edu.uc.lp3.client;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import py.edu.uc.lp3exceptions.CompaneroNoDisponibleException;
import py.edu.uc.lp3exceptions.RechazoDeCompaneroException;

/**
 * Base de los clientes HTTP hacia los compañeros. Solo conoce lo que publica
 * su OpenAPI (ruta, parámetros, códigos): no hay ninguna clase de su dominio acá.
 * Traduce lo que pasa en la red a la jerarquía propia:
 * 4xx → RechazoDeCompaneroException (su regla dijo que no);
 * 404/405, 5xx, sin conexión o timeout → CompaneroNoDisponibleException.
 */
public abstract class ClienteCompanero {

    private static final int MAXIMO_RESPUESTA = 500;

    protected final RestClient http;
    private final String rol;
    private final String nombre;
    private final String baseUrl;

    protected ClienteCompanero(String rol, String nombre, String baseUrl, int timeoutMs) {
        this.rol = rol;
        this.nombre = nombre;
        this.baseUrl = baseUrl;
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(Duration.ofMillis(timeoutMs));
        fabrica.setReadTimeout(Duration.ofMillis(timeoutMs));
        this.http = RestClient.builder().baseUrl(baseUrl).requestFactory(fabrica).build();
    }

    /** Ejecuta la llamada y convierte los errores de red en excepciones atribuidas a A2 o A3. */
    protected <T> T consultar(String rutaConParametros, Supplier<T> llamada) {
        String url = baseUrl + rutaConParametros;
        try {
            return llamada.get();
        } catch (HttpClientErrorException e) {
            int status = e.getStatusCode().value();
            String cuerpo = recortar(e.getResponseBodyAsString(StandardCharsets.UTF_8));
            if (status == 404 || status == 405) {
                throw new CompaneroNoDisponibleException(rol, nombre, url, status, cuerpo,
                        "la ruta no existe (revisar companeros." + rol.toLowerCase() + ".ruta contra su OpenAPI)");
            }
            throw new RechazoDeCompaneroException(rol, nombre, url, status, cuerpo);
        } catch (HttpServerErrorException e) {
            throw new CompaneroNoDisponibleException(rol, nombre, url, e.getStatusCode().value(),
                    recortar(e.getResponseBodyAsString(StandardCharsets.UTF_8)), "respondió un error interno");
        } catch (ResourceAccessException e) {
            throw new CompaneroNoDisponibleException(rol, nombre, url, null, null,
                    "no responde en " + baseUrl + " (¿está levantado?)");
        }
    }

    protected CompaneroNoDisponibleException respuestaInesperada(String rutaConParametros, String detalle) {
        return new CompaneroNoDisponibleException(rol, nombre, baseUrl + rutaConParametros, 200, null,
                "la respuesta no coincide con su OpenAPI (" + detalle + ")");
    }

    protected static String recortar(String texto) {
        if (texto == null || texto.length() <= MAXIMO_RESPUESTA) {
            return texto;
        }
        return texto.substring(0, MAXIMO_RESPUESTA) + "…";
    }

    public String getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
