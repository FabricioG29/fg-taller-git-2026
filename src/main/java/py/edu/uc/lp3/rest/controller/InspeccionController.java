package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.service.InspeccionService;
import py.edu.uc.lp3.service.dto.FilaInspeccion;

/** Endpoint del protocolo Inspeccionable. La consulta la arma InspeccionService. */
@RestController
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
