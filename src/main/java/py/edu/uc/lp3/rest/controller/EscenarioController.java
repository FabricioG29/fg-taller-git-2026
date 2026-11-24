package py.edu.uc.lp3.rest.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.constants.EjemplosOpenApi;
import py.edu.uc.lp3.domain.Jugada;
import py.edu.uc.lp3.rest.handler.ErrorRespuesta;
import py.edu.uc.lp3.rest.handler.RechazoRespuesta;
import py.edu.uc.lp3.service.EscenarioService;
import py.edu.uc.lp3.service.dto.EscenarioGranadaRequest;

/** Escenario cruzado de POO-09. Solo traduce HTTP; el orden y las reglas están en EscenarioService. */
@RestController
@Tag(name = "Escenario (POO-09)", description = "Lanzar una granada a un rival: se guarda solo si A2 (Matias Enciso, "
        + "daño por distancia) y A3 (Diana Aquino, fuego amigo y alcance) aceptan. Si uno rechaza, H2 no cambia.")
public class EscenarioController {

    private final EscenarioService escenarioService;

    public EscenarioController(EscenarioService escenarioService) {
        this.escenarioService = escenarioService;
    }

    @PostMapping(ApiPaths.ESCENARIO_GRANADA)
    @Operation(summary = "Lanzar una granada (consulta A2 y luego A3)",
            description = "Primero mis reglas (granada sin usar, jugadores vivos). Después A2 mide el daño a esa "
                    + "distancia y A3 decide si el lanzamiento es legal. Con las dos aceptaciones se gasta la granada, "
                    + "se aplica el chaleco y se guarda la jugada.")
    @ApiResponse(responseCode = "201", description = "A2 y A3 aceptaron: jugada guardada")
    @ApiResponse(responseCode = "409", description = "RECHAZO_A2 / RECHAZO_A3 (o GRANADA_USADA, regla propia)",
            content = @Content(schema = @Schema(implementation = RechazoRespuesta.class), examples = {
                    @ExampleObject(name = "RECHAZO_A2", value = EjemplosOpenApi.RECHAZO_A2),
                    @ExampleObject(name = "RECHAZO_A3", value = EjemplosOpenApi.RECHAZO_A3),
                    @ExampleObject(name = "GRANADA_USADA", value = EjemplosOpenApi.GRANADA_USADA)}))
    @ApiResponse(responseCode = "503", description = "A2 o A3 no responde (COMPANERO_NO_DISPONIBLE_A2/A3)",
            content = @Content(schema = @Schema(implementation = RechazoRespuesta.class),
                    examples = @ExampleObject(name = "NO_DISPONIBLE", value = EjemplosOpenApi.NO_DISPONIBLE)))
    @ApiResponse(responseCode = "400", description = "Falta un campo o el arma no es granada (VALOR_INVALIDO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = EjemplosOpenApi.VALOR_INVALIDO)))
    @ApiResponse(responseCode = "404", description = "Jugador o granada inexistente (RECURSO_NO_ENCONTRADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = EjemplosOpenApi.NO_ENCONTRADO)))
    @ApiResponse(responseCode = "422", description = "Atacante u objetivo eliminado (JUGADOR_ELIMINADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = EjemplosOpenApi.JUGADOR_ELIMINADO)))
    public ResponseEntity<Jugada> lanzarGranada(@RequestBody EscenarioGranadaRequest pedido) {
        Jugada jugada = escenarioService.lanzarGranada(pedido);
        return ResponseEntity.created(URI.create(ApiPaths.JUGADAS + "/" + jugada.getId())).body(jugada);
    }

    @GetMapping(ApiPaths.JUGADAS)
    @Operation(summary = "Jugadas guardadas (solo las que A2 y A3 aceptaron)")
    public List<Jugada> jugadas() {
        return escenarioService.jugadas();
    }

    @GetMapping(ApiPaths.JUGADAS + ApiPaths.ID)
    @Operation(summary = "Una jugada por id")
    @ApiResponse(responseCode = "200", description = "La jugada")
    @ApiResponse(responseCode = "404", description = "No existe (RECURSO_NO_ENCONTRADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = EjemplosOpenApi.NO_ENCONTRADO)))
    public Jugada jugada(@PathVariable("id") Long id) {
        return escenarioService.jugada(id);
    }
}
