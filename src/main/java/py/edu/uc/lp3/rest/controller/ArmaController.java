package py.edu.uc.lp3.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.service.ArmaService;
import py.edu.uc.lp3.service.dto.PruebaPistola;

/**
 * POO-06: construye una Pistola con los parámetros de la URL (sin guardarla).
 * El controller no valida ni corrige: lo hace ArmaService.
 */
@RestController
@RequestMapping(ApiPaths.ARMAS)
public class ArmaController {

    private final ArmaService armaService;

    public ArmaController(ArmaService armaService) {
        this.armaService = armaService;
    }

    @GetMapping(ApiPaths.PISTOLA)
    public PruebaPistola crearPistola(
            @RequestParam String nombre,
            @RequestParam int precio,
            @RequestParam Equipo equipo,
            @RequestParam int danio,
            @RequestParam(defaultValue = "0.9") float precision,
            @RequestParam(defaultValue = "20") int cargador,
            @RequestParam(defaultValue = "120") int reserva) {

        return armaService.probarPistola(nombre, precio, equipo, danio, precision, cargador, reserva);
    }
}
