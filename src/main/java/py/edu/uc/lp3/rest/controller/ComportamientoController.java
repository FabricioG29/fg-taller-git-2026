package py.edu.uc.lp3.rest.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.Inventario;
import py.edu.uc.lp3.service.ArmaService;

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
    public Map<String, Object> comportamientos() {
        Inventario inventario = armaService.inventarioDeEjemplo();

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("tienda", inventario.mostrarTienda());
        respuesta.put("usar", inventario.usarTodas());       // método abstracto: cada hija responde distinto
        respuesta.put("recargar", inventario.recargarTodas());
        return respuesta;
    }

    @GetMapping(ApiPaths.TIENDA)
    public Map<String, Object> tienda() {
        return Map.of("tienda", armaService.inventarioDeEjemplo().mostrarTienda());
    }
}
