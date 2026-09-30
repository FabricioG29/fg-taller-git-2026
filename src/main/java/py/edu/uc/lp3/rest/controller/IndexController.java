package py.edu.uc.lp3.rest.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;

/** Portero del servicio: confirma que está vivo, quién lo hizo y de qué dominio es. */
@RestController
public class IndexController {

    @GetMapping(ApiPaths.INDEX)
    public Map<String, Object> index() {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("servicio", "fg-taller-git-2026");
        respuesta.put("estado", "UP");
        respuesta.put("autor", "Fabricio González");
        respuesta.put("asignatura", "Lenguaje de Programación 3 (CYT646)");
        respuesta.put("dominio", "Counter-Strike 2");
        respuesta.put("endpoints", List.of(
                "GET " + ApiPaths.INDEX,
                "GET " + ApiPaths.ARMAS + ApiPaths.PISTOLA
                        + "?nombre=&precio=&equipo=&danio=&precision=&cargador=&reserva=",
                "GET " + ApiPaths.ARMAS + ApiPaths.COMPORTAMIENTOS,
                "GET " + ApiPaths.ARMAS + ApiPaths.TIENDA));
        return respuesta;
    }
}
