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
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.rest.dto.ActualizarArmaRequest;
import py.edu.uc.lp3.rest.dto.CrearArmaRequest;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * CRUD REST de la familia Arma. El recurso es "armas"; el verbo HTTP dice la operación.
 * El controller depende de la interface ArmaRepository (no de una clase concreta)
 * y de los mensajes del dominio: no valida ni asigna campos a mano.
 */
@RestController
@RequestMapping(ApiPaths.ARMAS)
public class ArmaCrudController {

    private final ArmaRepository armaRepository;

    public ArmaCrudController(ArmaRepository armaRepository) {
        this.armaRepository = armaRepository;
    }

    /** POST /api/armas → 201 con el arma creada y su id (lo asigna la base). */
    @PostMapping
    public ResponseEntity<Arma> crear(@RequestBody CrearArmaRequest pedido) {
        Arma arma = armaRepository.save(pedido.crear()); // si el constructor rechaza, no se guarda nada
        return ResponseEntity.created(URI.create(ApiPaths.ARMAS + "/" + arma.getId())).body(arma);
    }

    /** GET /api/armas[?equipo=TERRORISTAS | ?precioMaximo=2000] → lo que está en la tabla. */
    @GetMapping
    public List<Arma> listar(@RequestParam(value = "equipo", required = false) Equipo equipo,
                             @RequestParam(value = "precioMaximo", required = false) Integer precioMaximo) {
        if (equipo != null) {
            return armaRepository.findByEquipoOrderByIdAsc(equipo);
        }
        if (precioMaximo != null) {
            return armaRepository.findByPrecioLessThanEqualOrderByPrecioAsc(precioMaximo);
        }
        return armaRepository.findAll();
    }

    /** GET /api/armas/{id} → 200 o 404. */
    @GetMapping(ApiPaths.ID)
    public Arma buscar(@PathVariable("id") Long id) {
        return buscarOFallar(id);
    }

    /** PATCH /api/armas/{id} → cambia solo los campos enviados, por mensajes del dominio. */
    @PatchMapping(ApiPaths.ID)
    public Arma actualizar(@PathVariable("id") Long id, @RequestBody ActualizarArmaRequest pedido) {
        if (pedido.nombre() == null && pedido.precio() == null) {
            throw new ValorInvalidoException("Nada para actualizar: enviá \"nombre\" y/o \"precio\"");
        }
        Arma arma = buscarOFallar(id);
        if (pedido.nombre() != null) {
            arma.renombrar(pedido.nombre());
        }
        if (pedido.precio() != null) {
            arma.cambiarPrecio(pedido.precio());
        }
        return armaRepository.save(arma);
    }

    /** DELETE /api/armas/{id} → 204; el arma sale del juego. 404 si no existe. */
    @DeleteMapping(ApiPaths.ID)
    public ResponseEntity<Void> borrar(@PathVariable("id") Long id) {
        armaRepository.delete(buscarOFallar(id));
        return ResponseEntity.noContent().build();
    }

    private Arma buscarOFallar(Long id) {
        return armaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Arma", id));
    }
}
