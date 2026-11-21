package py.edu.uc.lp3.rest.handler;

import io.swagger.v3.oas.annotations.media.Schema;

/** Cuerpo de todos los errores: mismo formato para cada tipo de la jerarquía. */
@Schema(description = "Error del servicio. 'error' es el código estable que conviene leer desde un cliente.")
public record ErrorRespuesta(
        @Schema(description = "Código HTTP", example = "409") int status,
        @Schema(description = "Código de la regla o del error", example = "SIN_MUNICION",
                allowableValues = {"VALOR_INVALIDO", "JSON_INVALIDO", "RECURSO_NO_ENCONTRADO",
                        "SIN_MUNICION", "GRANADA_USADA", "JUGADOR_ELIMINADO"}) String error,
        @Schema(description = "Clase de la excepción", example = "SinMunicionException") String excepcion,
        @Schema(description = "Explicación para una persona",
                example = "P250: cargador vacío, hay que recargar") String mensaje) {
}
