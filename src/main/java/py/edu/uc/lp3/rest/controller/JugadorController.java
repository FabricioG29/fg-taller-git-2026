package py.edu.uc.lp3.rest.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.service.JugadorService;
import py.edu.uc.lp3.service.dto.CrearJugadorRequest;

/** Alta y consulta de jugadores, siempre a través de JugadorService. */
@RestController
@RequestMapping(ApiPaths.JUGADORES)
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @PostMapping
    public ResponseEntity<Jugador> crear(@RequestBody CrearJugadorRequest pedido) {
        Jugador jugador = jugadorService.crear(pedido);
        return ResponseEntity.created(URI.create(ApiPaths.JUGADORES + "/" + jugador.getId())).body(jugador);
    }

    @GetMapping
    public List<Jugador> listar(@RequestParam(value = "vivos", defaultValue = "false") boolean soloVivos) {
        return jugadorService.listar(soloVivos);
    }

    @GetMapping(ApiPaths.ID)
    public Jugador buscar(@PathVariable("id") Long id) {
        return jugadorService.buscar(id);
    }
}
