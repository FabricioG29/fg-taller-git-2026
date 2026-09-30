package py.edu.uc.lp3.service;

import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.Inventario;
import py.edu.uc.lp3.domain.Pistola;

/** Casos de uso del dominio CS2 que expone la API. */
public interface ArmaService {

    Pistola crearPistola(String nombre, int precio, Equipo equipo, int danio,
                         float precision, int cargador, int reserva);

    Inventario inventarioDeEjemplo();
}
