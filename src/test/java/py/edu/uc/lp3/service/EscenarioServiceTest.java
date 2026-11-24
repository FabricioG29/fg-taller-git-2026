package py.edu.uc.lp3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import py.edu.uc.lp3.client.CompanerosFalsos;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.domain.Jugada;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.service.dto.CrearArmaRequest.GranadaRequest;
import py.edu.uc.lp3.service.dto.CrearJugadorRequest;
import py.edu.uc.lp3.service.dto.EscenarioGranadaRequest;
import py.edu.uc.lp3exceptions.GranadaUsadaException;
import py.edu.uc.lp3exceptions.RechazoDeCompaneroException;

/**
 * El escenario completo contra dobles HTTP de A2 y A3 (base H2 propia en memoria):
 * se guarda solo si los dos aceptan; si uno rechaza, se atribuye y la base no cambia.
 */
@SpringBootTest
class EscenarioServiceTest {

    private static final CompanerosFalsos FALSOS = crear();

    private static CompanerosFalsos crear() {
        try {
            return new CompanerosFalsos();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @DynamicPropertySource
    static void companeros(DynamicPropertyRegistry registro) {
        registro.add("companeros.a2.base-url", FALSOS::urlA2);
        registro.add("companeros.a3.base-url", FALSOS::urlA3);
        registro.add("spring.datasource.url", () -> "jdbc:h2:mem:escenario;DB_CLOSE_DELAY=-1");
    }

    @AfterAll
    static void apagar() {
        FALSOS.close();
    }

    @Autowired
    private EscenarioService escenario;

    @Autowired
    private ArmaService armaService;

    @Autowired
    private JugadorService jugadorService;

    private Jugador jugador(String nombre, Equipo equipo) {
        return jugadorService.crear(new CrearJugadorRequest(nombre, equipo, 100, 100));
    }

    private Arma granada() {
        return armaService.crear(new GranadaRequest("HE Grenade", 300, Equipo.AMBOS, 98, 5.0f, 1.5f, "Fragmentación"));
    }

    @Test
    void siA2YA3AceptanSeGuardaLaJugadaYSeGastaLaGranada() {
        Jugador ct = jugador("CT-Feliz", Equipo.ANTITERRORISTAS);
        Jugador t = jugador("T-Feliz", Equipo.TERRORISTAS);
        Arma he = granada();
        int antes = escenario.jugadas().size();

        // A2 (doble): radio 8 m, a 2 m llega el 75% → 98 * 0.75 = 74; el chaleco absorbe 37
        Jugada jugada = escenario.lanzarGranada(new EscenarioGranadaRequest(ct.getId(), t.getId(), he.getId(), 2.0));

        assertEquals(74, jugada.getDanioExplosion());
        assertEquals(63, jugadorService.buscar(t.getId()).getVida());
        assertTrue(((Granada) armaService.buscar(he.getId())).isUsada());
        assertEquals(antes + 1, escenario.jugadas().size());
        assertEquals(jugada.getId(), escenario.jugada(jugada.getId()).getId());

        // la misma granada no se puede lanzar otra vez (regla propia, antes de consultar a nadie)
        assertThrows(GranadaUsadaException.class, () -> escenario.lanzarGranada(
                new EscenarioGranadaRequest(ct.getId(), t.getId(), he.getId(), 2.0)));
    }

    @Test
    void siA2RechazaSeLeAtribuyeYLaBaseQuedaIgual() {
        Jugador ct = jugador("CT-A2", Equipo.ANTITERRORISTAS);
        Jugador t = jugador("T-A2", Equipo.TERRORISTAS);
        Arma he = granada();
        int antes = escenario.jugadas().size();

        RechazoDeCompaneroException e = assertThrows(RechazoDeCompaneroException.class, () -> escenario
                .lanzarGranada(new EscenarioGranadaRequest(ct.getId(), t.getId(), he.getId(), -3.0)));

        assertEquals("A2", e.getRol());
        sinCambios(antes, t, he);
    }

    @Test
    void siA3RechazaElFuegoAmigoSeLeAtribuyeYLaBaseQuedaIgual() {
        Jugador t1 = jugador("T1-A3", Equipo.TERRORISTAS);
        Jugador t2 = jugador("T2-A3", Equipo.TERRORISTAS);
        Arma he = granada();
        int antes = escenario.jugadas().size();

        RechazoDeCompaneroException e = assertThrows(RechazoDeCompaneroException.class, () -> escenario
                .lanzarGranada(new EscenarioGranadaRequest(t1.getId(), t2.getId(), he.getId(), 2.0)));

        assertEquals("A3", e.getRol());
        assertEquals(422, (int) e.getStatusCompanero());
        sinCambios(antes, t2, he);
    }

    private void sinCambios(int jugadasAntes, Jugador objetivo, Arma granada) {
        assertEquals(jugadasAntes, escenario.jugadas().size());
        assertEquals(100, jugadorService.buscar(objetivo.getId()).getVida());
        assertEquals(100, jugadorService.buscar(objetivo.getId()).getArmadura());
        assertEquals(false, ((Granada) armaService.buscar(granada.getId())).isUsada());
    }
}
