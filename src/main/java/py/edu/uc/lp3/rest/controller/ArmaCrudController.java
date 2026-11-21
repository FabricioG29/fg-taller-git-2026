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
public class ArmaCrudController {

    private final ArmaService armaService;

    public ArmaCrudController(ArmaService armaService) {
        this.armaService = armaService;
    }

    @PostMapping
    public ResponseEntity<Arma> crear(@RequestBody CrearArmaRequest pedido) {
        Arma arma = armaService.crear(pedido);
        return ResponseEntity.created(URI.create(ApiPaths.ARMAS + "/" + arma.getId())).body(arma);
    }

    @GetMapping
    public List<Arma> listar(@RequestParam(value = "equipo", required = false) Equipo equipo,
                             @RequestParam(value = "precioMaximo", required = false) Integer precioMaximo) {
        return armaService.listar(equipo, precioMaximo);
    }

    @GetMapping(ApiPaths.ID)
    public Arma buscar(@PathVariable("id") Long id) {
        return armaService.buscar(id);
    }

    @PatchMapping(ApiPaths.ID)
    public Arma actualizar(@PathVariable("id") Long id, @RequestBody ActualizarArmaRequest pedido) {
        return armaService.actualizar(id, pedido);
    }

    @DeleteMapping(ApiPaths.ID)
    public ResponseEntity<Void> borrar(@PathVariable("id") Long id) {
        armaService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
