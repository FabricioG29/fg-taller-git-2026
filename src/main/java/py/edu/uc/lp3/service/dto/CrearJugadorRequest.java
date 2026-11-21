package py.edu.uc.lp3.service.dto;

import py.edu.uc.lp3.domain.Equipo;

/** Cuerpo del POST /api/jugadores. Sin vida/armadura, JugadorService usa el constructor simple. */
public record CrearJugadorRequest(String nombre, Equipo equipo, Integer vida, Integer armadura) {
}
