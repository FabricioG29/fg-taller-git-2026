package py.edu.uc.lp3.domain;

import java.util.List;

/** Lo que devuelve la simulación: el estado final de las tres clases después de persistir. */
public record ReporteSimulacion(Jugador atacante, Jugador objetivo, Arma arma, List<Disparo> disparos) {
}
