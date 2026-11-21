package py.edu.uc.lp3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * Cabecera del documento OpenAPI 3 (GET /v3/api-docs, UI en /swagger-ui.html).
 * Los endpoints los descubre springdoc; acá va lo que un compañero necesita leer
 * antes de invocar: las dos reglas y el formato de los errores.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI documentoOpenApi() {
        return new OpenAPI().info(new Info()
                .title("fg-taller-git-2026 — CS2 (armas, jugadores y simulación)")
                .version("POO-09")
                .contact(new Contact().name("Fabricio González (FabricioG29)")
                        .url("https://github.com/FabricioG29/fg-taller-git-2026"))
                .description("""
                        Servicio de Counter-Strike 2 con Spring Boot 4 y H2.

                        **Regla 1 — Munición.** Cada disparo gasta balas del cargador; con el cargador vacío \
                        el arma no dispara (409 `SIN_MUNICION`) hasta recargar desde la reserva. \
                        Una granada es un consumible: se lanza una vez y un segundo uso responde 409 `GRANADA_USADA`.

                        **Regla 2 — Daño con chaleco.** El chaleco absorbe la mitad del daño (hasta lo que le queda) \
                        y se gasta; el resto baja la vida, que nunca queda negativa. Con vida 0 el jugador está \
                        eliminado: no dispara ni recibe daño (422 `JUGADOR_ELIMINADO`).

                        **Para consultar las reglas sin crear nada:** `POST /api/reglas/municion` y \
                        `POST /api/reglas/danio` (sin estado, no tocan la base).

                        **Errores.** Todos responden `{"status","error","excepcion","mensaje"}`: \
                        400 `VALOR_INVALIDO` / `JSON_INVALIDO`, 404 `RECURSO_NO_ENCONTRADO`, \
                        409 `SIN_MUNICION` / `GRANADA_USADA`, 422 `JUGADOR_ELIMINADO`."""));
    }
}
