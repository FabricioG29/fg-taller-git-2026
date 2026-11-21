package py.edu.uc.lp3.service.dto;

import py.edu.uc.lp3.domain.ResultadoAccion;

/** Resultado de GET /api/armas/pistola (POO-06): la pistola armada y su primer disparo. */
public record PruebaPistola(String creada, String municion, ResultadoAccion primerDisparo) {
}
