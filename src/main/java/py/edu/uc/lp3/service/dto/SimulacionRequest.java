package py.edu.uc.lp3.service.dto;

/** Cuerpo del POST /api/simulaciones/disparos. "veces" es opcional (1 por defecto). */
public record SimulacionRequest(Long atacanteId, Long objetivoId, Long armaId, Integer veces) {
}
