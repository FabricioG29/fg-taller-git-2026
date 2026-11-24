package py.edu.uc.lp3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Inspeccionable;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.repository.DisparoRepository;
import py.edu.uc.lp3.repository.JugadaRepository;
import py.edu.uc.lp3.repository.JugadorRepository;
import py.edu.uc.lp3.service.InspeccionService;
import py.edu.uc.lp3.service.dto.FilaInspeccion;

/**
 * Junta objetos de familias distintas en una List&lt;Inspeccionable&gt; y les manda
 * el mismo mensaje, sin preguntar el tipo.
 */
@Service
public class InspeccionServiceImpl implements InspeccionService {

    private final ArmaRepository armaRepository;
    private final JugadorRepository jugadorRepository;
    private final DisparoRepository disparoRepository;
    private final JugadaRepository jugadaRepository;

    public InspeccionServiceImpl(ArmaRepository armaRepository, JugadorRepository jugadorRepository,
                                 DisparoRepository disparoRepository, JugadaRepository jugadaRepository) {
        this.armaRepository = armaRepository;
        this.jugadorRepository = jugadorRepository;
        this.disparoRepository = disparoRepository;
        this.jugadaRepository = jugadaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaInspeccion> inspeccionarTodo() {
        List<Inspeccionable> objetos = new ArrayList<>();
        objetos.addAll(jugadorRepository.findAll());
        objetos.addAll(armaRepository.findAll());
        objetos.addAll(disparoRepository.findAll());
        objetos.addAll(jugadaRepository.findAll());

        List<FilaInspeccion> filas = new ArrayList<>();
        for (Inspeccionable objeto : objetos) {
            filas.add(new FilaInspeccion(objeto.getClass().getSimpleName(), objeto.inspeccionar()));
        }
        return filas;
    }
}
