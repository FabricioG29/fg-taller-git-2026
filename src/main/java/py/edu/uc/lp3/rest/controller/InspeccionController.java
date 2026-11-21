package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.service.InspeccionService;
import py.edu.uc.lp3.service.dto.FilaInspeccion;

/** Endpoint del protocolo Inspeccionable. La consulta la arma InspeccionService. */
@RestController
@Tag(name = "Inspección", description = "Protocolo Inspeccionable sobre todo lo persistido.")
public class InspeccionController {

    private final InspeccionService inspeccionService;

    public InspeccionController(InspeccionService inspeccionService) {
        this.inspeccionService = inspeccionService;
    }

    @GetMapping(ApiPaths.INSPECCION)
    public List<FilaInspeccion> inspeccionar() {
        return inspeccionService.inspeccionarTodo();
    }
}
