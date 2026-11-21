package py.edu.uc.lp3.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.repository.JugadorRepository;
import py.edu.uc.lp3.service.JugadorService;
import py.edu.uc.lp3.service.dto.CrearJugadorRequest;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;

/** Alta y consulta de jugadores: única capa que usa el JugadorRepository. */
@Service
public class JugadorServiceImpl implements JugadorService {

    private final JugadorRepository jugadorRepository;

    public JugadorServiceImpl(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @Override
    @Transactional
    public Jugador crear(CrearJugadorRequest pedido) {
        return jugadorRepository.save(pedido.crear());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Jugador> listar(boolean soloVivos) {
        return soloVivos ? jugadorRepository.findByVidaGreaterThanOrderByIdAsc(0) : jugadorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Jugador buscar(Long id) {
        return jugadorRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Jugador", id));
    }
}
