package py.edu.uc.lp3.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.cs2.Equipo;
import py.edu.uc.lp3.cs2.Pistola;

/**
 * Construye una Pistola con los parámetros de la URL.
 * El controller NO valida ni corrige: si un valor es ilegal, lo rechaza
 * el constructor de la clase y ManejoErrores responde 400.
 */
@RestController
@RequestMapping("/api/armas")
public class ArmaController {

    @GetMapping("/pistola")
    public Map<String, Object> crearPistola(
            @RequestParam String nombre,
            @RequestParam int precio,
            @RequestParam Equipo equipo,
            @RequestParam int danio,
            @RequestParam(defaultValue = "0.9") float precision,
            @RequestParam(defaultValue = "20") int cargador,
            @RequestParam(defaultValue = "120") int reserva) {

        Pistola pistola = new Pistola(nombre, precio, equipo, danio, precision, cargador, reserva);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("creada", pistola.mostrarEnTienda());
        respuesta.put("municion", pistola.getMunicionCargador() + "/" + pistola.getMunicionReserva());
        respuesta.put("primerDisparo", pistola.usar());
        return respuesta;
    }
}
