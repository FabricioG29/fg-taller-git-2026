package py.edu.uc.lp3.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.client.ClienteExplosionA2;
import py.edu.uc.lp3.client.ClienteGranadaA3;
import py.edu.uc.lp3.client.ExplosionA2;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.domain.Impacto;
import py.edu.uc.lp3.domain.Jugada;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.repository.JugadaRepository;
import py.edu.uc.lp3.repository.JugadorRepository;
import py.edu.uc.lp3.service.EscenarioService;
import py.edu.uc.lp3.service.ReglasDeJuegoService;
import py.edu.uc.lp3.service.dto.EscenarioGranadaRequest;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * «Lanzar una granada HE a un rival». Orden:
 * 1. Reglas propias (A1), sin red: existen los jugadores y la granada, la granada no
 *    está usada (409), nadie está eliminado (422) y el atacante puede usarla.
 * 2. A2 (Matias Enciso): cuánto daño de la explosión llega a esa distancia. Va primero
 *    porque es una consulta de solo lectura y descarta lo físicamente imposible
 *    (distancia negativa) antes de preguntar si el lanzamiento es legal.
 * 3. A3 (Diana Aquino): si el lanzamiento es legal en la partida (no es fuego amigo y
 *    está dentro de los 35 m). Es la última puerta antes de escribir.
 * 4. Solo si los dos aceptaron: se gasta la granada, se aplica mi regla del chaleco al
 *    daño atenuado y se guarda la jugada. Nada se escribe antes de las dos respuestas,
 *    y @Transactional deshace todo si algo falla al guardar: si uno rechaza, H2 queda igual.
 */
@Service
public class EscenarioServiceImpl implements EscenarioService {

    private final JugadorRepository jugadorRepository;
    private final ArmaRepository armaRepository;
    private final JugadaRepository jugadaRepository;
    private final ReglasDeJuegoService reglas;
    private final ClienteExplosionA2 a2;
    private final ClienteGranadaA3 a3;

    public EscenarioServiceImpl(JugadorRepository jugadorRepository, ArmaRepository armaRepository,
                                JugadaRepository jugadaRepository, ReglasDeJuegoService reglas,
                                ClienteExplosionA2 a2, ClienteGranadaA3 a3) {
        this.jugadorRepository = jugadorRepository;
        this.armaRepository = armaRepository;
        this.jugadaRepository = jugadaRepository;
        this.reglas = reglas;
        this.a2 = a2;
        this.a3 = a3;
    }

    @Override
    @Transactional
    public Jugada lanzarGranada(EscenarioGranadaRequest pedido) {
        // 1. Reglas propias, sin red
        Validador.requerido(pedido, "cuerpo");
        Long atacanteId = Validador.requerido(pedido.atacanteId(), "atacanteId");
        Long objetivoId = Validador.requerido(pedido.objetivoId(), "objetivoId");
        Long granadaId = Validador.requerido(pedido.granadaId(), "granadaId");
        double distancia = Validador.requerido(pedido.distancia(), "distancia");
        if (!Double.isFinite(distancia)) {
            throw new ValorInvalidoException("distancia tiene que ser un número");
        }
        if (atacanteId.equals(objetivoId)) {
            throw new ValorInvalidoException("Un jugador no puede lanzarse una granada a sí mismo");
        }
        Jugador atacante = buscarJugador(atacanteId);
        Jugador objetivo = buscarJugador(objetivoId);
        Arma arma = armaRepository.findById(granadaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Arma", granadaId));
        if (!(arma instanceof Granada granada)) {
            throw new ValorInvalidoException(arma.getNombre() + " no es una granada");
        }
        reglas.verificarQuePuedeDisparar(atacante, granada);
        reglas.verificarQuePuedeRecibirDanio(objetivo);
        reglas.verificarQuePuedeUsarse(granada);

        // 2. A2: daño que llega a esa distancia
        boolean mismoEquipo = atacante.getEquipo() == objetivo.getEquipo();
        ExplosionA2 explosion = a2.medirExplosion(distancia, objetivo.getArmadura() > 0, mismoEquipo);

        // 3. A3: ¿es legal el lanzamiento?
        String respuestaA3 = a3.validarLanzamiento(atacante.getEquipo(), objetivo.getEquipo(), distancia);

        // 4. Los dos aceptaron: recién ahora cambia el estado
        double factor = explosion.factorDeDistancia();
        int danioGranada = granada.lanzar().danio();
        int danio = (int) Math.round(danioGranada * factor);
        Impacto impacto = reglas.calcularImpacto(objetivo.getVida(), objetivo.getArmadura(), danio);
        objetivo.aplicarImpacto(impacto);
        String respuestaA2 = "enRadioAlcance=" + explosion.enRadioAlcance() + ", danioNominal="
                + explosion.danioNominal() + ", danioAtenuadoDistancia=" + explosion.danioAtenuadoDistancia();
        return jugadaRepository.save(new Jugada(atacante, objetivo, granada, distancia, factor, impacto,
                respuestaA2, respuestaA3));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Jugada> jugadas() {
        return jugadaRepository.findAllByOrderByIdAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Jugada jugada(Long id) {
        return jugadaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Jugada", id));
    }

    private Jugador buscarJugador(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Jugador", id));
    }
}
