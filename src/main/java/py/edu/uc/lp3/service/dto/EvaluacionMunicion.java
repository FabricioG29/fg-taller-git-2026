package py.edu.uc.lp3.service.dto;

/** Respuesta de la regla de munición cuando la ráfaga alcanza (si no alcanza, 409 SIN_MUNICION). */
public record EvaluacionMunicion(boolean permitido, int cargadorAntes, int balasNecesarias, int cargadorDespues) {
}
