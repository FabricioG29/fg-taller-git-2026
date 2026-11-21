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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.constants.EjemplosOpenApi;
import py.edu.uc.lp3.rest.handler.ErrorRespuesta;
import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.service.JugadorService;
import py.edu.uc.lp3.service.dto.CrearJugadorRequest;

/** Alta y consulta de jugadores, siempre a través de JugadorService. */
@RestController
@RequestMapping(ApiPaths.JUGADORES)
@Tag(name = "Jugadores", description = "Jugadores de CS2: vida 1..100, chaleco 0..100, equipo TERRORISTAS o ANTITERRORISTAS.")
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @Operation(summary = "Crear un jugador")
    @ApiResponse(responseCode = "201", description = "Creado")
    @ApiResponse(responseCode = "400", description = "Valor fuera de rango o campo faltante (VALOR_INVALIDO) / JSON ilegible, tipo o equipo desconocido (JSON_INVALIDO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "VALOR_INVALIDO", value = EjemplosOpenApi.VALOR_INVALIDO), @ExampleObject(name = "JSON_INVALIDO", value = EjemplosOpenApi.JSON_INVALIDO)}))
    @PostMapping
    public ResponseEntity<Jugador> crear(@RequestBody CrearJugadorRequest pedido) {
        Jugador jugador = jugadorService.crear(pedido);
        return ResponseEntity.created(URI.create(ApiPaths.JUGADORES + "/" + jugador.getId())).body(jugador);
    }

    @Operation(summary = "Listar jugadores (vivos=true para solo los vivos)")
    @ApiResponse(responseCode = "200", description = "Jugadores")
    @GetMapping
    public List<Jugador> listar(@RequestParam(value = "vivos", defaultValue = "false") boolean soloVivos) {
        return jugadorService.listar(soloVivos);
    }

    @Operation(summary = "Buscar un jugador por id")
    @ApiResponse(responseCode = "200", description = "El jugador")
    @ApiResponse(responseCode = "404", description = "El id no existe (RECURSO_NO_ENCONTRADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "NO_ENCONTRADO", value = EjemplosOpenApi.NO_ENCONTRADO)}))
    @GetMapping(ApiPaths.ID)
    public Jugador buscar(@PathVariable("id") Long id) {
        return jugadorService.buscar(id);
    }
}
