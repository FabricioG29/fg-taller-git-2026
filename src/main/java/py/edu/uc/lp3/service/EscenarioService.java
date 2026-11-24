package py.edu.uc.lp3.service;

import java.util.List;

import py.edu.uc.lp3.domain.Jugada;
import py.edu.uc.lp3.service.dto.EscenarioGranadaRequest;

/**
 * Escenario cruzado de POO-09: «lanzar una granada a un rival».
 * Se guarda solo si A2 (daño por distancia) y A3 (lanzamiento legal) aceptan.
 */
public interface EscenarioService {

    Jugada lanzarGranada(EscenarioGranadaRequest pedido);

    List<Jugada> jugadas();

    Jugada jugada(Long id);
}
