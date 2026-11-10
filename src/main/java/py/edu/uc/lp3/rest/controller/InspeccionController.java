package py.edu.uc.lp3.rest.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.Inspeccionable;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.repository.DisparoRepository;
import py.edu.uc.lp3.repository.JugadorRepository;

/**
 * Endpoint del protocolo: junta objetos de familias distintas (armas y jugadores)
 * (y el historial de disparos) en una sola List&lt;Inspeccionable&gt; y les manda el mismo mensaje, sin preguntar el tipo.
 */
@RestController
public class InspeccionController {

    private final ArmaRepository armaRepository;
    private final JugadorRepository jugadorRepository;
    private final DisparoRepository disparoRepository;

    public InspeccionController(ArmaRepository armaRepository, JugadorRepository jugadorRepository,
                                DisparoRepository disparoRepository) {
        this.armaRepository = armaRepository;
        this.jugadorRepository = jugadorRepository;
        this.disparoRepository = disparoRepository;
    }

    @GetMapping(ApiPaths.INSPECCION)
    public List<Map<String, String>> inspeccionar() {
        List<Inspeccionable> objetos = new ArrayList<>();
        objetos.addAll(jugadorRepository.findAll());
        objetos.addAll(armaRepository.findAll());
        objetos.addAll(disparoRepository.findAll());

        List<Map<String, String>> respuesta = new ArrayList<>();
        for (Inspeccionable objeto : objetos) {
            Map<String, String> fila = new LinkedHashMap<>();
            fila.put("clase", objeto.getClass().getSimpleName());
            fila.put("inspeccion", objeto.inspeccionar()); // polimorfismo por la interface
            respuesta.add(fila);
        }
        return respuesta;
    }
}
