package py.edu.uc.lp3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.EstadoUso;
import py.edu.uc.lp3.domain.Impacto;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.service.impl.ReglasDeJuegoServiceImpl;
import py.edu.uc.lp3exceptions.JugadorEliminadoException;
import py.edu.uc.lp3exceptions.SinMunicionException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** Las dos reglas, probadas en la capa service, sin Spring ni base. */
class ReglasDeJuegoServiceTest {

    private final ReglasDeJuegoService reglas = new ReglasDeJuegoServiceImpl();

    @Test
    void reglaDeMunicionRechazaElCargadorVacio() {
        Pistola p250 = new Pistola("P250", 300, Equipo.AMBOS, 30, 0.9f, 1, 0);

        reglas.verificarQuePuedeUsarse(p250);
        assertEquals(27, p250.usar().danio());
        assertEquals(EstadoUso.SIN_MUNICION, p250.estadoDeUso());
        assertThrows(SinMunicionException.class, () -> reglas.verificarQuePuedeUsarse(p250));
    }

    @Test
    void recargarPasaBalasDeLaReservaSinPasarseDeLaCapacidad() {
        Pistola glock = new Pistola("Glock-18", 200, Equipo.TERRORISTAS, 30); // 20/120
        glock.disparar(3);
        glock.recargar();

        assertEquals(20, glock.getMunicionCargador());
        assertEquals(117, glock.getMunicionReserva());
    }

    @Test
    void reglaDeDanioElChalecoAbsorbeLaMitad() {
        Impacto impacto = reglas.calcularImpacto(100, 100, 27);

        assertEquals(13, impacto.absorbidoPorArmadura());
        assertEquals(14, impacto.danioVida());
        assertEquals(86, impacto.vidaRestante());
        assertEquals(87, impacto.armaduraRestante());
    }

    @Test
    void laVidaNoQuedaNegativaYUnEliminadoNoRecibeMasDanio() {
        Impacto impacto = reglas.calcularImpacto(1, 0, 500);
        assertEquals(0, impacto.vidaRestante());
        assertTrue(impacto.eliminado());

        Jugador bot = new Jugador("Bot", Equipo.ANTITERRORISTAS, 1, 0);
        bot.aplicarImpacto(impacto);
        assertThrows(JugadorEliminadoException.class, () -> reglas.verificarQuePuedeRecibirDanio(bot));
        assertThrows(JugadorEliminadoException.class, () -> reglas.calcularImpacto(0, 0, 10));
    }

    @Test
    void unJugadorNoUsaArmasDelOtroBando() {
        Jugador ct = new Jugador("Ana", Equipo.ANTITERRORISTAS);
        Pistola glock = new Pistola("Glock-18", 200, Equipo.TERRORISTAS, 30);

        assertThrows(ValorInvalidoException.class, () -> reglas.verificarQuePuedeDisparar(ct, glock));
    }

    @Test
    void laReglaDeDanioRechazaValoresImposibles() {
        assertThrows(ValorInvalidoException.class, () -> reglas.calcularImpacto(100, 100, -5));
        assertThrows(ValorInvalidoException.class, () -> reglas.calcularImpacto(500, 0, 10));
        assertThrows(ValorInvalidoException.class, () -> reglas.calcularImpacto(100, -1, 10));
    }
}
