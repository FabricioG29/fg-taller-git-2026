package py.edu.uc.lp3.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Inventario de un jugador. Guarda cualquier Arma y le habla siempre
 * como Arma: no pregunta el tipo concreto (sin instanceof, sin if por tipo).
 */
public class Inventario {

    public static final int MAXIMO_ARMAS = 5;

    private final List<Arma> armas = new ArrayList<>();

    /** Agrega el arma si hay lugar. Devuelve false si es nula o si el inventario está lleno. */
    public boolean agregar(Arma arma) {
        if (arma == null || armas.size() >= MAXIMO_ARMAS) {
            return false;
        }
        return armas.add(arma);
    }

    public List<ResultadoAccion> usarTodas() {
        List<ResultadoAccion> resultados = new ArrayList<>();
        for (Arma arma : armas) {
            resultados.add(arma.usar()); // polimorfismo: cada una responde a su manera
        }
        return resultados;
    }

    public List<ResultadoAccion> recargarTodas() {
        List<ResultadoAccion> resultados = new ArrayList<>();
        for (Arma arma : armas) {
            resultados.add(arma.recargar());
        }
        return resultados;
    }

    public List<String> mostrarTienda() {
        List<String> lineas = new ArrayList<>();
        for (Arma arma : armas) {
            lineas.add(arma.mostrarEnTienda());
        }
        return lineas;
    }

    public List<Arma> getArmas() {
        return Collections.unmodifiableList(armas);
    }
}
