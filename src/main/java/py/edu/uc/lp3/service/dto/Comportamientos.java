package py.edu.uc.lp3.service.dto;

import java.util.List;

import py.edu.uc.lp3.domain.ResultadoAccion;

/** Resultado de GET /api/armas/comportamientos (POO-06): tienda, usar() y recargar() de cada arma. */
public record Comportamientos(List<String> tienda, List<ResultadoAccion> usar, List<ResultadoAccion> recargar) {
}
