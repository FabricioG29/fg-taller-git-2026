package py.edu.uc.lp3.rest.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.constants.ApiPaths;

/** Portero del servicio: confirma que está vivo, quién lo hizo y de qué dominio es. */
@RestController
@Tag(name = "Servicio", description = "Portero: confirma que el servicio está vivo.")
public class IndexController {

    @GetMapping(ApiPaths.INDEX)
    public Map<String, Object> index() {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("servicio", "fg-taller-git-2026");
        respuesta.put("estado", "UP");
        respuesta.put("autor", "Fabricio González");
        respuesta.put("asignatura", "Lenguaje de Programación 3 (CYT646)");
        respuesta.put("dominio", "Counter-Strike 2");
        respuesta.put("persistencia", "H2 (consola en /h2-console)");
        respuesta.put("openapi", "/v3/api-docs  (UI: /swagger-ui.html)");
        respuesta.put("endpoints", List.of(
                "GET " + ApiPaths.INDEX,
                "POST | GET " + ApiPaths.ARMAS + "  (CRUD de la familia Arma)",
                "GET | PATCH | DELETE " + ApiPaths.ARMAS + "/{id}",
                "POST | GET " + ApiPaths.JUGADORES,
                "GET " + ApiPaths.JUGADORES + "/{id}",
                "GET " + ApiPaths.INSPECCION + "  (protocolo Inspeccionable)",
                "POST " + ApiPaths.SIMULACION_DISPAROS + "  (simulación: munición + daño)",
                "POST " + ApiPaths.ARMAS + "/{id}" + ApiPaths.RECARGA,
                "GET " + ApiPaths.DISPAROS + "?objetivoId=",
                "POST " + ApiPaths.REGLAS + ApiPaths.REGLA_MUNICION + "  (regla 1, sin estado)",
                "POST " + ApiPaths.REGLAS + ApiPaths.REGLA_DANIO + "  (regla 2, sin estado)",
                "POST " + ApiPaths.ESCENARIO_GRANADA + "  (escenario: consulta a A2 y A3)",
                "GET " + ApiPaths.JUGADAS + "[/{id}]",
                "GET " + ApiPaths.ARMAS + ApiPaths.PISTOLA
                        + "?nombre=&precio=&equipo=&danio=&precision=&cargador=&reserva=",
                "GET " + ApiPaths.ARMAS + ApiPaths.COMPORTAMIENTOS,
                "GET " + ApiPaths.ARMAS + ApiPaths.TIENDA));
        return respuesta;
    }
}
