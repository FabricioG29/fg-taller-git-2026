package py.edu.uc.lp3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Disparo;
import py.edu.uc.lp3.domain.Impacto;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.domain.ReporteSimulacion;
import py.edu.uc.lp3.domain.ResultadoAccion;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.repository.DisparoRepository;
import py.edu.uc.lp3.repository.JugadorRepository;
import py.edu.uc.lp3.service.ReglasDeJuegoService;
import py.edu.uc.lp3.service.SimulacionService;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * Orquesta la simulación de disparos: le pide a ReglasDeJuegoService que aplique
 * la regla de munición y la de daño, cambia el estado de las entidades con sus
 * mensajes (usar, aplicarImpacto) y guarda lo que pasó.
 * @Transactional: si una regla lanza una excepción a mitad de camino, se deshace
 * todo (munición, vida y disparos ya registrados). La base queda como antes.
 */
@Service
public class SimulacionServiceImpl implements SimulacionService {

    public static final int MAXIMO_DISPAROS = 30;

    private final ArmaRepository armaRepository;
    private final JugadorRepository jugadorRepository;
    private final DisparoRepository disparoRepository;
    private final ReglasDeJuegoService reglas;

    public SimulacionServiceImpl(ArmaRepository armaRepository, JugadorRepository jugadorRepository,
                                 DisparoRepository disparoRepository, ReglasDeJuegoService reglas) {
        this.armaRepository = armaRepository;
        this.jugadorRepository = jugadorRepository;
        this.disparoRepository = disparoRepository;
        this.reglas = reglas;
    }

    @Override
    @Transactional
    public ReporteSimulacion simularDisparos(Long atacanteId, Long objetivoId, Long armaId, Integer vecesPedidas) {
        Validador.requerido(atacanteId, "atacanteId");
        Validador.requerido(objetivoId, "objetivoId");
        Validador.requerido(armaId, "armaId");
        int veces = vecesPedidas == null ? 1 : vecesPedidas;
        if (veces < 1 || veces > MAXIMO_DISPAROS) {
            throw new ValorInvalidoException("veces debe estar entre 1 y " + MAXIMO_DISPAROS);
        }
        if (atacanteId.equals(objetivoId)) {
            throw new ValorInvalidoException("Un jugador no puede dispararse a sí mismo");
        }
        Jugador atacante = buscarJugador(atacanteId);
        Jugador objetivo = buscarJugador(objetivoId);
        Arma arma = armaRepository.findById(armaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Arma", armaId));

        reglas.verificarQuePuedeDisparar(atacante, arma);   // vivo y arma de su bando
        reglas.verificarQuePuedeRecibirDanio(objetivo);     // no se le dispara a un eliminado

        List<Disparo> disparos = new ArrayList<>();
        for (int i = 0; i < veces && !objetivo.estaEliminado(); i++) {
            reglas.verificarQuePuedeUsarse(arma);                           // regla 1: munición
            ResultadoAccion resultado = arma.usar();                        // cada hija se usa a su modo
            Impacto impacto = reglas.calcularImpacto(objetivo.getVida(), objetivo.getArmadura(),
                    resultado.danio());                                     // regla 2: chaleco y vida
            objetivo.aplicarImpacto(impacto);
            disparos.add(disparoRepository.save(new Disparo(atacante, objetivo, arma, resultado, impacto)));
        }
        armaRepository.save(arma);
        jugadorRepository.save(objetivo);
        return new ReporteSimulacion(atacante, objetivo, arma, disparos);
    }

    @Override
    @Transactional
    public Arma recargar(Long armaId) {
        Arma arma = armaRepository.findById(armaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Arma", armaId));
        arma.recargar();
        return armaRepository.save(arma);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disparo> historial(Long objetivoId, Long atacanteId) {
        if (objetivoId != null) {
            return disparoRepository.findByObjetivoIdOrderByIdAsc(objetivoId);
        }
        if (atacanteId != null) {
            return disparoRepository.findByAtacanteIdOrderByIdAsc(atacanteId);
        }
        return disparoRepository.findAll();
    }

    private Jugador buscarJugador(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Jugador", id));
    }
}
