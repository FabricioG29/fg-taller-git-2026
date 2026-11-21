package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Disparo;
import py.edu.uc.lp3.domain.ReporteSimulacion;
import py.edu.uc.lp3.service.dto.SimulacionRequest;
import py.edu.uc.lp3.service.SimulacionService;

/**
 * Entrada HTTP de la simulación. Solo dispara los casos de uso: las reglas de
 * munición y de daño están en ReglasDeJuegoService (capa service).
 */
@RestController
@Tag(name = "Simulación", description = "Aplica las dos reglas en una transacción: si una falla, no se guarda nada.")
public class SimulacionController {

    private final SimulacionService simulacionService;

    public SimulacionController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    /** POST /api/simulaciones/disparos → 201: crea los disparos y devuelve el estado ya persistido. */
    @Operation(summary = "Simular disparos (regla 1 + regla 2)",
            description = "El atacante usa el arma \"veces\" (1..30) contra el objetivo. Cada uso gasta munición (regla 1) y el daño pasa por el chaleco (regla 2). Una granada se lanza una sola vez.")
    @ApiResponse(responseCode = "201", description = "Disparos guardados y estado final del arma y del objetivo")
    @ApiResponse(responseCode = "400", description = "Valor fuera de rango o campo faltante (VALOR_INVALIDO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "VALOR_INVALIDO", value = EjemplosOpenApi.VALOR_INVALIDO)}))
    @ApiResponse(responseCode = "404", description = "El id no existe (RECURSO_NO_ENCONTRADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "NO_ENCONTRADO", value = EjemplosOpenApi.NO_ENCONTRADO)}))
    @ApiResponse(responseCode = "409", description = "Regla 1: cargador vacío (SIN_MUNICION) / Regla 1: la granada ya se lanzó (GRANADA_USADA)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "SIN_MUNICION", value = EjemplosOpenApi.SIN_MUNICION), @ExampleObject(name = "GRANADA_USADA", value = EjemplosOpenApi.GRANADA_USADA)}))
    @ApiResponse(responseCode = "422", description = "Regla 2: atacante u objetivo eliminado (JUGADOR_ELIMINADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "JUGADOR_ELIMINADO", value = EjemplosOpenApi.JUGADOR_ELIMINADO)}))
    @PostMapping(ApiPaths.SIMULACION_DISPAROS)
    public ResponseEntity<ReporteSimulacion> simular(@RequestBody SimulacionRequest pedido) {
        ReporteSimulacion reporte = simulacionService.simularDisparos(
                pedido.atacanteId(), pedido.objetivoId(), pedido.armaId(), pedido.veces());
        return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
    }

    /** POST /api/armas/{id}/recarga → carga el cargador desde la reserva. */
    @Operation(summary = "Recargar un arma desde la reserva")
    @ApiResponse(responseCode = "200", description = "Arma con el cargador lleno")
    @ApiResponse(responseCode = "404", description = "El id no existe (RECURSO_NO_ENCONTRADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "NO_ENCONTRADO", value = EjemplosOpenApi.NO_ENCONTRADO)}))
    @PostMapping(ApiPaths.ARMAS + ApiPaths.ID + ApiPaths.RECARGA)
    public Arma recargar(@PathVariable("id") Long id) {
        return simulacionService.recargar(id);
    }

    /** GET /api/disparos[?objetivoId=2 | ?atacanteId=1] → historial persistido. */
    @Operation(summary = "Historial de disparos (por objetivo o atacante)")
    @ApiResponse(responseCode = "200", description = "Disparos persistidos")
    @GetMapping(ApiPaths.DISPAROS)
    public List<Disparo> historial(@RequestParam(value = "objetivoId", required = false) Long objetivoId,
                                   @RequestParam(value = "atacanteId", required = false) Long atacanteId) {
        return simulacionService.historial(objetivoId, atacanteId);
    }
}
