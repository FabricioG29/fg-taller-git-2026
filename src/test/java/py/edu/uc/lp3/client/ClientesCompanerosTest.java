package py.edu.uc.lp3.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3exceptions.CompaneroNoDisponibleException;
import py.edu.uc.lp3exceptions.RechazoDeCompaneroException;

/** Los clientes contra dobles HTTP reales: acepta, rechaza (atribuido) y no disponible. */
class ClientesCompanerosTest {

    private static CompanerosFalsos falsos;

    @BeforeAll
    static void levantar() throws Exception {
        falsos = new CompanerosFalsos();
    }

    @AfterAll
    static void apagar() {
        falsos.close();
    }

    private ClienteExplosionA2 a2(String url) {
        return new ClienteExplosionA2("Matias Enciso", url, "/api/simulacion/explosion", 1000);
    }

    private ClienteGranadaA3 a3(String url) {
        return new ClienteGranadaA3("Diana Aquino", url, "/api/armas/granadas/simular",
                "Terrorista", "Counter-Terrorista", 1000);
    }

    @Test
    void a2AceptaYDevuelveElFactorDeDistancia() {
        ExplosionA2 explosion = a2(falsos.urlA2()).medirExplosion(2.0, true, false);
        assertEquals(0.75, explosion.factorDeDistancia(), 0.001);
    }

    @Test
    void a2RechazaUnaDistanciaNegativaYSeLeAtribuye() {
        RechazoDeCompaneroException e = assertThrows(RechazoDeCompaneroException.class,
                () -> a2(falsos.urlA2()).medirExplosion(-1.0, true, false));
        assertEquals("A2", e.getRol());
        assertEquals("RECHAZO_A2", e.getCodigo());
        assertEquals(400, (int) e.getStatusCompanero());
    }

    @Test
    void a3RechazaElFuegoAmigoYSeLeAtribuye() {
        assertTrue(a3(falsos.urlA3()).validarLanzamiento(Equipo.TERRORISTAS, Equipo.ANTITERRORISTAS, 2.0)
                .contains("válida"));
        RechazoDeCompaneroException e = assertThrows(RechazoDeCompaneroException.class,
                () -> a3(falsos.urlA3()).validarLanzamiento(Equipo.TERRORISTAS, Equipo.TERRORISTAS, 2.0));
        assertEquals("A3", e.getRol());
        assertEquals(422, (int) e.getStatusCompanero());
    }

    @Test
    void unCompaneroApagadoQuedaComoNoDisponible() {
        CompaneroNoDisponibleException e = assertThrows(CompaneroNoDisponibleException.class,
                () -> a2("http://localhost:9").medirExplosion(2.0, true, false));
        assertEquals("A2", e.getRol());
        assertEquals("COMPANERO_NO_DISPONIBLE_A2", e.getCodigo());
    }
}
