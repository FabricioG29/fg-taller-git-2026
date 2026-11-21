package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.service.ArmaService;
import py.edu.uc.lp3.service.dto.Comportamientos;

/**
 * Pide los mensajes del contrato de Arma (mostrarEnTienda, usar, recargar)
 * a cada arma tratándola como Arma. No hay if por tipo.
 */
@RestController
@RequestMapping(ApiPaths.ARMAS)
public class ComportamientoController {

    private final ArmaService armaService;

    public ComportamientoController(ArmaService armaService) {
        this.armaService = armaService;
    }

    @GetMapping(ApiPaths.COMPORTAMIENTOS)
    public Comportamientos comportamientos() {
        return armaService.comportamientosDeEjemplo(); // cada hija responde distinto a usar() y recargar()
    }

    @GetMapping(ApiPaths.TIENDA)
    public Map<String, List<String>> tienda() {
        return Map.of("tienda", armaService.tiendaDeEjemplo());
    }
}
