package py.edu.uc.lp3.service.dto;

import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** Cuerpo del POST /api/jugadores. Sin vida/armadura usa el constructor simple. */
public record CrearJugadorRequest(String nombre, Equipo equipo, Integer vida, Integer armadura) {

    public Jugador crear() {
        if (vida == null && armadura == null) {
            return new Jugador(nombre, equipo);
        }
        if (vida == null || armadura == null) {
            throw new ValorInvalidoException("vida y armadura se envían juntas (o ninguna)");
        }
        return new Jugador(nombre, equipo, vida, armadura);
    }
}
