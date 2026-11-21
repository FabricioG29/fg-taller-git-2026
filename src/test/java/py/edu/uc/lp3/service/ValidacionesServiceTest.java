package py.edu.uc.lp3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.service.dto.ActualizarArmaRequest;
import py.edu.uc.lp3.service.dto.CrearArmaRequest.PistolaRequest;
import py.edu.uc.lp3.service.dto.CrearJugadorRequest;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** Las validaciones viven en los services: un valor ilegal no llega a la base (H2 en memoria). */
@SpringBootTest
@Transactional
class ValidacionesServiceTest {

    @Autowired
    private ArmaService armaService;

    @Autowired
    private JugadorService jugadorService;

    @Test
    void unArmaIlegalNoSeGuarda() {
        int antes = armaService.listar(null, null).size();

        assertThrows(ValorInvalidoException.class, () -> armaService.crear(
                new PistolaRequest("Rota", 200, Equipo.TERRORISTAS, -5, null, null, null)));
        assertThrows(ValorInvalidoException.class, () -> armaService.crear(
                new PistolaRequest("NaN", 200, Equipo.TERRORISTAS, 30, Float.NaN, 20, 120)));
        assertThrows(ValorInvalidoException.class, () -> armaService.crear(
                new PistolaRequest(" ", 200, Equipo.TERRORISTAS, 30, null, null, null)));

        assertEquals(antes, armaService.listar(null, null).size());
    }

    @Test
    void unPatchIlegalNoCambiaElArma() {
        Arma glock = armaService.crear(new PistolaRequest("Glock-18", 200, Equipo.TERRORISTAS, 30, null, null, null));

        assertThrows(ValorInvalidoException.class,
                () -> armaService.actualizar(glock.getId(), new ActualizarArmaRequest(null, -1)));
        assertEquals(200, armaService.buscar(glock.getId()).obtenerPrecio());
    }

    @Test
    void unJugadorNoPuedeSerDeAmbosNiNacerMuerto() {
        assertThrows(ValorInvalidoException.class,
                () -> jugadorService.crear(new CrearJugadorRequest("X", Equipo.AMBOS, null, null)));
        assertThrows(ValorInvalidoException.class,
                () -> jugadorService.crear(new CrearJugadorRequest("X", Equipo.TERRORISTAS, 0, 50)));
    }
}
