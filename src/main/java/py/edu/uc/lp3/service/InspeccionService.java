package py.edu.uc.lp3.service;

import java.util.List;

import py.edu.uc.lp3.service.dto.FilaInspeccion;

/** Protocolo Inspeccionable sobre todo lo persistido (armas, jugadores y disparos). */
public interface InspeccionService {

    List<FilaInspeccion> inspeccionarTodo();
}
