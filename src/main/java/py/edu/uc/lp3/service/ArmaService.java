package py.edu.uc.lp3.service;

import java.util.List;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.service.dto.ActualizarArmaRequest;
import py.edu.uc.lp3.service.dto.Comportamientos;
import py.edu.uc.lp3.service.dto.CrearArmaRequest;
import py.edu.uc.lp3.service.dto.PruebaPistola;

/** Casos de uso de la familia Arma. Es la única puerta entre los controllers y el ArmaRepository. */
public interface ArmaService {

    Arma crear(CrearArmaRequest pedido);

    List<Arma> listar(Equipo equipo, Integer precioMaximo);

    Arma buscar(Long id);

    Arma actualizar(Long id, ActualizarArmaRequest pedido);

    void borrar(Long id);

    // --- Endpoints de POO-06 (sin persistencia) ---

    PruebaPistola probarPistola(String nombre, int precio, Equipo equipo, int danio,
                                float precision, int cargador, int reserva);

    Comportamientos comportamientosDeEjemplo();

    List<String> tiendaDeEjemplo();
}
