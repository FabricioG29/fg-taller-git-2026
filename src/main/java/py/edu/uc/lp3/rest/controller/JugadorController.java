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
import py.edu.uc.lp3.repository.JugadorRepository;
import py.edu.uc.lp3.rest.dto.CrearJugadorRequest;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;

/** Alta y consulta de jugadores (los participantes de la simulación). */
@RestController
@RequestMapping(ApiPaths.JUGADORES)
public class JugadorController {

    private final JugadorRepository jugadorRepository;

    public JugadorController(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @PostMapping
    public ResponseEntity<Jugador> crear(@RequestBody CrearJugadorRequest pedido) {
        Jugador jugador = jugadorRepository.save(pedido.crear());
        return ResponseEntity.created(URI.create(ApiPaths.JUGADORES + "/" + jugador.getId())).body(jugador);
    }

    /** GET /api/jugadores[?vivos=true] */
    @GetMapping
    public List<Jugador> listar(@RequestParam(value = "vivos", defaultValue = "false") boolean soloVivos) {
        return soloVivos ? jugadorRepository.findByVidaGreaterThanOrderByIdAsc(0) : jugadorRepository.findAll();
    }

    @GetMapping(ApiPaths.ID)
    public Jugador buscar(@PathVariable("id") Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Jugador", id));
    }
}
