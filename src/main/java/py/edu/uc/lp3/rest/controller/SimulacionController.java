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

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Disparo;
import py.edu.uc.lp3.domain.ReporteSimulacion;
import py.edu.uc.lp3.service.dto.SimulacionRequest;
import py.edu.uc.lp3.service.SimulacionService;

/**
 * Entrada HTTP de la simulación. Solo dispara los casos de uso: las reglas de
 * munición y de daño están en ArmaDeFuego y en Jugador.
 */
@RestController
public class SimulacionController {

    private final SimulacionService simulacionService;

    public SimulacionController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    /** POST /api/simulaciones/disparos → 201: crea los disparos y devuelve el estado ya persistido. */
    @PostMapping(ApiPaths.SIMULACION_DISPAROS)
    public ResponseEntity<ReporteSimulacion> simular(@RequestBody SimulacionRequest pedido) {
        pedido.validar();
        ReporteSimulacion reporte = simulacionService.simularDisparos(
                pedido.atacanteId(), pedido.objetivoId(), pedido.armaId(), pedido.vecesOUna());
        return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
    }

    /** POST /api/armas/{id}/recarga → carga el cargador desde la reserva. */
    @PostMapping(ApiPaths.ARMAS + ApiPaths.ID + ApiPaths.RECARGA)
    public Arma recargar(@PathVariable("id") Long id) {
        return simulacionService.recargar(id);
    }

    /** GET /api/disparos[?objetivoId=2 | ?atacanteId=1] → historial persistido. */
    @GetMapping(ApiPaths.DISPAROS)
    public List<Disparo> historial(@RequestParam(value = "objetivoId", required = false) Long objetivoId,
                                   @RequestParam(value = "atacanteId", required = false) Long atacanteId) {
        return simulacionService.historial(objetivoId, atacanteId);
    }
}
