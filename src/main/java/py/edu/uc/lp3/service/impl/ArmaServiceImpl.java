package py.edu.uc.lp3.service.impl;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.domain.Inventario;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.service.ArmaService;

/**
 * Arma los objetos del dominio. No valida nada: las reglas viven en las clases
 * (si un valor es ilegal, el constructor lanza IllegalArgumentException).
 */
@Service
public class ArmaServiceImpl implements ArmaService {

    @Override
    public Pistola crearPistola(String nombre, int precio, Equipo equipo, int danio,
                                float precision, int cargador, int reserva) {
        return new Pistola(nombre, precio, equipo, danio, precision, cargador, reserva);
    }

    @Override
    public Inventario inventarioDeEjemplo() {
        Inventario inventario = new Inventario();
        inventario.agregar(new Pistola("Glock-18", 200, Equipo.TERRORISTAS, 30)); // constructor sobrecargado
        inventario.agregar(new Granada("HE Grenade", 300, Equipo.AMBOS, 98, 5.0f, 1.5f,
                "Explosión de fragmentación", 3000));
        return inventario;
    }
}
