package py.edu.uc.lp3.service;

import java.util.List;

import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.service.dto.CrearJugadorRequest;

/** Casos de uso de jugadores. Es la única puerta hacia el JugadorRepository. */
public interface JugadorService {

    Jugador crear(CrearJugadorRequest pedido);

    List<Jugador> listar(boolean soloVivos);

    Jugador buscar(Long id);
}
