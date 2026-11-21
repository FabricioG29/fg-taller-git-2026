package py.edu.uc.lp3.service.dto;

import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** Cuerpo del POST /api/simulaciones/disparos. "veces" es opcional (1 por defecto). */
public record SimulacionRequest(Long atacanteId, Long objetivoId, Long armaId, Integer veces) {

    public void validar() {
        if (atacanteId == null || objetivoId == null || armaId == null) {
            throw new ValorInvalidoException("atacanteId, objetivoId y armaId son obligatorios");
        }
    }

    public int vecesOUna() {
        return veces == null ? 1 : veces;
    }
}
