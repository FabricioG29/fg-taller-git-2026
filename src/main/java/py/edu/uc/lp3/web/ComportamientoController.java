package py.edu.uc.lp3.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.cs2.Equipo;
import py.edu.uc.lp3.cs2.Granada;
import py.edu.uc.lp3.cs2.Inventario;
import py.edu.uc.lp3.cs2.Pistola;
import py.edu.uc.lp3.cs2.ResultadoAccion;

/**
 * Parte F: pide el mensaje abstracto usar() a cada arma tratándola como Arma.
 * No hay if por tipo: cada objeto informa su propio comportamiento.
 */
@RestController
@RequestMapping("/api/armas")
public class ComportamientoController {

    private Inventario inventarioDeEjemplo() {
        Inventario inventario = new Inventario();
        inventario.agregar(new Pistola("Glock-18", 200, Equipo.TERRORISTAS, 30, 0.9f, 20, 120));
        inventario.agregar(new Granada("HE Grenade", 300, Equipo.AMBOS, 98, 5.0f, 1.5f,
                "Explosión de fragmentación", 3000));
        return inventario;
    }

    @GetMapping("/comportamientos")
    public Map<String, Object> comportamientos() {
        Inventario inventario = inventarioDeEjemplo();
        List<String> tienda = inventario.mostrarTienda();
        List<ResultadoAccion> usos = inventario.usarTodas();
        List<ResultadoAccion> recargas = inventario.recargarTodas();

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("tienda", tienda);
        respuesta.put("usar", usos);         // método abstracto: cada hija responde distinto
        respuesta.put("recargar", recargas);
        return respuesta;
    }

    @GetMapping("/tienda")
    public Map<String, Object> tienda() {
        return Map.of("tienda", inventarioDeEjemplo().mostrarTienda());
    }
}
