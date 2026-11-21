package py.edu.uc.lp3.service;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Impacto;
import py.edu.uc.lp3.domain.Jugador;

/**
 * Las dos reglas del juego. Viven en la capa service: deciden y lanzan la
 * excepción de la jerarquía; las entidades solo guardan el estado.
 */
public interface ReglasDeJuegoService {

    /**
     * REGLA 1 (munición): el arma tiene que estar lista para usarse. Un arma de fuego
     * necesita balas en el cargador (SinMunicionException); una granada no puede estar
     * ya lanzada (GranadaUsadaException).
     */
    void verificarQuePuedeUsarse(Arma arma);

    /** REGLA 2 (daño): el atacante tiene que estar vivo y el arma ser de su bando (o de AMBOS). */
    void verificarQuePuedeDisparar(Jugador atacante, Arma arma);

    /** REGLA 2 (daño): a un jugador eliminado no se le puede seguir disparando. */
    void verificarQuePuedeRecibirDanio(Jugador objetivo);

    /**
     * REGLA 2 (daño con chaleco): el chaleco absorbe la mitad del daño (hasta lo que le
     * queda) y se gasta; el resto baja la vida, que nunca queda negativa.
     */
    Impacto calcularImpacto(int vida, int armadura, int danio);
}
