package py.edu.uc.lp3.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.repository.JugadorRepository;
import py.edu.uc.lp3.service.JugadorService;
import py.edu.uc.lp3.service.dto.CrearJugadorRequest;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** Alta y consulta de jugadores: única capa que usa el JugadorRepository y la que valida al jugador. */
@Service
public class JugadorServiceImpl implements JugadorService {

    private final JugadorRepository jugadorRepository;

    public JugadorServiceImpl(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @Override
    @Transactional
    public Jugador crear(CrearJugadorRequest pedido) {
        Validador.requerido(pedido, "cuerpo");
        String nombre = Validador.texto(pedido.nombre(), "nombre", 40);
        Equipo equipo = Validador.requerido(pedido.equipo(), "equipo");
        if (equipo == Equipo.AMBOS) {
            throw new ValorInvalidoException("Un jugador juega para TERRORISTAS o ANTITERRORISTAS, no para AMBOS");
        }
        if (pedido.vida() == null && pedido.armadura() == null) {
            return jugadorRepository.save(new Jugador(nombre, equipo)); // constructor simple
        }
        if (pedido.vida() == null || pedido.armadura() == null) {
            throw new ValorInvalidoException("vida y armadura se envían juntas (o ninguna)");
        }
        int vida = Validador.entero(pedido.vida(), "vida", 1, Jugador.VIDA_MAXIMA);
        int armadura = Validador.entero(pedido.armadura(), "armadura", 0, Jugador.ARMADURA_MAXIMA);
        return jugadorRepository.save(new Jugador(nombre, equipo, vida, armadura)); // sobrecargado
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
