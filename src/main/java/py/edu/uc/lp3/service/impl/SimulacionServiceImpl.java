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
import py.edu.uc.lp3.service.SimulacionService;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * Orquesta la simulación de disparos. No calcula daño ni toca munición: le manda
 * mensajes al Arma (regla 1) y al Jugador (regla 2) y guarda lo que pasó.
 * @Transactional: si una regla lanza una excepción a mitad de camino, se deshace
 * todo (munición, vida y disparos ya registrados). La base queda como antes.
 */
@Service
public class SimulacionServiceImpl implements SimulacionService {

    public static final int MAXIMO_DISPAROS = 30;

    private final ArmaRepository armaRepository;
    private final JugadorRepository jugadorRepository;
    private final DisparoRepository disparoRepository;

    public SimulacionServiceImpl(ArmaRepository armaRepository, JugadorRepository jugadorRepository,
                                 DisparoRepository disparoRepository) {
        this.armaRepository = armaRepository;
        this.jugadorRepository = jugadorRepository;
        this.disparoRepository = disparoRepository;
    }

    @Override
    @Transactional
    public ReporteSimulacion simularDisparos(Long atacanteId, Long objetivoId, Long armaId, int veces) {
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

        atacante.verificarQuePuedeDisparar(arma);   // vivo y arma de su bando
        objetivo.verificarQuePuedeRecibirDanio();   // no se le dispara a un eliminado

        List<Disparo> disparos = new ArrayList<>();
        for (int i = 0; i < veces && !objetivo.estaEliminado(); i++) {
            ResultadoAccion resultado = arma.usar();                    // regla 1: munición (polimórfico)
            Impacto impacto = objetivo.recibirDanio(resultado.danio()); // regla 2: chaleco y vida
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

    private Jugador buscarJugador(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Jugador", id));
    }
}
