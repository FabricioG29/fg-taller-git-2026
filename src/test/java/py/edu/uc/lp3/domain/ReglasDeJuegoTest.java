package py.edu.uc.lp3.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3exceptions.JugadorEliminadoException;
import py.edu.uc.lp3exceptions.SinMunicionException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** Pruebas de las dos reglas sin Spring ni base: solo objetos del dominio. */
class ReglasDeJuegoTest {

    @Test
    void reglaDeMunicionGastaBalasYCortaConCargadorVacio() {
        Pistola p250 = new Pistola("P250", 300, Equipo.AMBOS, 30, 0.9f, 1, 0);

        assertEquals(27, p250.usar().danio());
        assertEquals(0, p250.getMunicionCargador());
        assertThrows(SinMunicionException.class, p250::usar);
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
        Jugador rival = new Jugador("Rival", Equipo.ANTITERRORISTAS); // 100 / 100

        Impacto impacto = rival.recibirDanio(27);

        assertEquals(13, impacto.absorbidoPorArmadura());
        assertEquals(14, impacto.danioVida());
        assertEquals(86, rival.getVida());
        assertEquals(87, rival.getArmadura());
    }

    @Test
    void laVidaNoQuedaNegativaYUnEliminadoNoRecibeMasDanio() {
        Jugador bot = new Jugador("Bot", Equipo.ANTITERRORISTAS, 1, 0);

        Impacto impacto = bot.recibirDanio(500);

        assertEquals(0, bot.getVida());
        assertTrue(impacto.eliminado());
        assertThrows(JugadorEliminadoException.class, () -> bot.recibirDanio(10));
    }

    @Test
    void unJugadorNoUsaArmasDelOtroBando() {
        Jugador ct = new Jugador("Ana", Equipo.ANTITERRORISTAS);
        Pistola glock = new Pistola("Glock-18", 200, Equipo.TERRORISTAS, 30);

        assertThrows(ValorInvalidoException.class, () -> ct.verificarQuePuedeDisparar(glock));
        assertFalse(glock.esDelEquipo(Equipo.ANTITERRORISTAS));
    }

    @Test
    void ningunaFirmaDejaUnObjetoImposible() {
        assertThrows(ValorInvalidoException.class, () -> new Pistola("X", 200, Equipo.TERRORISTAS, -5));
        assertThrows(ValorInvalidoException.class, () -> new Jugador("X", Equipo.AMBOS));
        assertThrows(ValorInvalidoException.class, () -> new Jugador("X", Equipo.TERRORISTAS, 0, 50));
        Pistola glock = new Pistola("Glock-18", 200, Equipo.TERRORISTAS, 30);
        assertThrows(ValorInvalidoException.class, () -> glock.cambiarPrecio(-1));
        assertEquals(200, glock.obtenerPrecio());
    }
}
