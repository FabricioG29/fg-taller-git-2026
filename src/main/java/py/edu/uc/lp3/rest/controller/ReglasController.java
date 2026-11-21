package py.edu.uc.lp3.rest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.constants.EjemplosOpenApi;
import py.edu.uc.lp3.domain.Impacto;
import py.edu.uc.lp3.rest.handler.ErrorRespuesta;
import py.edu.uc.lp3.service.ReglasDeJuegoService;
import py.edu.uc.lp3.service.dto.EvaluacionMunicion;
import py.edu.uc.lp3.service.dto.ReglaDanioRequest;
import py.edu.uc.lp3.service.dto.ReglaMunicionRequest;

/**
 * Las dos reglas publicadas sin estado (POO-09): un compañero las consulta por HTTP
 * sin crear armas ni jugadores. Solo traduce HTTP; decide ReglasDeJuegoService.
 */
@RestController
@RequestMapping(ApiPaths.REGLAS)
@Tag(name = "Reglas", description = "Las dos reglas de CS2, sin estado: munición y daño con chaleco. "
        + "Pensadas para que otro servicio las consulte. 200 = acepta; 4xx = rechaza con el código de la regla.")
public class ReglasController {

    private final ReglasDeJuegoService reglas;

    public ReglasController(ReglasDeJuegoService reglas) {
        this.reglas = reglas;
    }

    @PostMapping(ApiPaths.REGLA_MUNICION)
    @Operation(summary = "Regla 1 — ¿alcanza la munición?",
            description = "Acepta si cargador ≥ balasPorDisparo × disparos y devuelve cómo queda el cargador. "
                    + "Si no alcanza, rechaza con 409 SIN_MUNICION (hay que recargar).")
    @ApiResponse(responseCode = "200", description = "La ráfaga alcanza",
            content = @Content(schema = @Schema(implementation = EvaluacionMunicion.class),
                    examples = @ExampleObject(value = """
                            {"permitido":true,"cargadorAntes":3,"balasNecesarias":2,"cargadorDespues":1}""")))
    @ApiResponse(responseCode = "409", description = "Cargador insuficiente (SIN_MUNICION)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = EjemplosOpenApi.SIN_MUNICION)))
    @ApiResponse(responseCode = "400", description = "Falta un campo o está fuera de rango (VALOR_INVALIDO / JSON_INVALIDO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = EjemplosOpenApi.VALOR_INVALIDO)))
    public EvaluacionMunicion municion(@RequestBody ReglaMunicionRequest pedido) {
        return reglas.evaluarMunicion(pedido);
    }

    @PostMapping(ApiPaths.REGLA_DANIO)
    @Operation(summary = "Regla 2 — daño con chaleco",
            description = "El chaleco absorbe la mitad del daño (hasta lo que le queda); el resto baja la vida, "
                    + "que nunca queda negativa. Si la vida ya es 0, rechaza con 422 JUGADOR_ELIMINADO.")
    @ApiResponse(responseCode = "200", description = "Impacto calculado",
            content = @Content(schema = @Schema(implementation = Impacto.class),
                    examples = @ExampleObject(value = """
                            {"danioRecibido":36,"absorbidoPorArmadura":18,"danioVida":18,"vidaRestante":82,"armaduraRestante":82,"eliminado":false}""")))
    @ApiResponse(responseCode = "422", description = "El jugador ya está eliminado (JUGADOR_ELIMINADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = EjemplosOpenApi.JUGADOR_ELIMINADO)))
    @ApiResponse(responseCode = "400", description = "Vida, chaleco o daño fuera de rango (VALOR_INVALIDO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(value = EjemplosOpenApi.VALOR_INVALIDO)))
    public Impacto danio(@RequestBody ReglaDanioRequest pedido) {
        return reglas.evaluarDanio(pedido);
    }
}
