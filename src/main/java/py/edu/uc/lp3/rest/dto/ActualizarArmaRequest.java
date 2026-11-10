package py.edu.uc.lp3.rest.dto;

/**
 * Cuerpo del PATCH /api/armas/{id}: solo los campos que se pueden cambiar
 * después de creada el arma. Un campo omitido (null) queda como estaba.
 */
public record ActualizarArmaRequest(String nombre, Integer precio) {
}
