package py.edu.uc.lp3.rest.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.service.ArmaService;
import py.edu.uc.lp3.service.dto.ActualizarArmaRequest;
import py.edu.uc.lp3.service.dto.CrearArmaRequest;

/**
 * CRUD REST de la familia Arma. Solo traduce HTTP: todo pasa por ArmaService,
 * que es quien valida y habla con el repositorio.
 */
@RestController
@RequestMapping(ApiPaths.ARMAS)
@Tag(name = "Armas (CRUD)", description = "Familia Arma persistida en H2 (SINGLE_TABLE). El campo \"tipo\" elige la hija: pistola, rifle, francotirador, escopeta, subfusil o granada.")
public class ArmaCrudController {

    private final ArmaService armaService;

    public ArmaCrudController(ArmaService armaService) {
        this.armaService = armaService;
    }

    @Operation(summary = "Crear un arma")
    @ApiResponse(responseCode = "201", description = "Creada; Location apunta al GET por id")
    @ApiResponse(responseCode = "400", description = "Valor fuera de rango o campo faltante (VALOR_INVALIDO) / JSON ilegible, tipo o equipo desconocido (JSON_INVALIDO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "VALOR_INVALIDO", value = EjemplosOpenApi.VALOR_INVALIDO), @ExampleObject(name = "JSON_INVALIDO", value = EjemplosOpenApi.JSON_INVALIDO)}))
    @PostMapping
    public ResponseEntity<Arma> crear(@RequestBody CrearArmaRequest pedido) {
        Arma arma = armaService.crear(pedido);
        return ResponseEntity.created(URI.create(ApiPaths.ARMAS + "/" + arma.getId())).body(arma);
    }

    @Operation(summary = "Listar armas (filtro opcional por equipo o precio máximo)")
    @ApiResponse(responseCode = "200", description = "Armas persistidas")
    @ApiResponse(responseCode = "400", description = "JSON ilegible, tipo o equipo desconocido (JSON_INVALIDO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "JSON_INVALIDO", value = EjemplosOpenApi.JSON_INVALIDO)}))
    @GetMapping
    public List<Arma> listar(@RequestParam(value = "equipo", required = false) Equipo equipo,
                             @RequestParam(value = "precioMaximo", required = false) Integer precioMaximo) {
        return armaService.listar(equipo, precioMaximo);
    }

    @Operation(summary = "Buscar un arma por id")
    @ApiResponse(responseCode = "200", description = "El arma")
    @ApiResponse(responseCode = "404", description = "El id no existe (RECURSO_NO_ENCONTRADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "NO_ENCONTRADO", value = EjemplosOpenApi.NO_ENCONTRADO)}))
    @GetMapping(ApiPaths.ID)
    public Arma buscar(@PathVariable("id") Long id) {
        return armaService.buscar(id);
    }

    @Operation(summary = "Cambiar nombre y/o precio")
    @ApiResponse(responseCode = "200", description = "Arma actualizada")
    @ApiResponse(responseCode = "400", description = "Valor fuera de rango o campo faltante (VALOR_INVALIDO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "VALOR_INVALIDO", value = EjemplosOpenApi.VALOR_INVALIDO)}))
    @ApiResponse(responseCode = "404", description = "El id no existe (RECURSO_NO_ENCONTRADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "NO_ENCONTRADO", value = EjemplosOpenApi.NO_ENCONTRADO)}))
    @PatchMapping(ApiPaths.ID)
    public Arma actualizar(@PathVariable("id") Long id, @RequestBody ActualizarArmaRequest pedido) {
        return armaService.actualizar(id, pedido);
    }

    @Operation(summary = "Borrar un arma")
    @ApiResponse(responseCode = "204", description = "Borrada")
    @ApiResponse(responseCode = "404", description = "El id no existe (RECURSO_NO_ENCONTRADO)",
            content = @Content(schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = {@ExampleObject(name = "NO_ENCONTRADO", value = EjemplosOpenApi.NO_ENCONTRADO)}))
    @DeleteMapping(ApiPaths.ID)
    public ResponseEntity<Void> borrar(@PathVariable("id") Long id) {
        armaService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
