package py.edu.uc.lp3.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Pregunta a la regla de daño con chaleco. No toca la base. */
@Schema(description = "Estado del jugador que recibe el daño y daño que llega")
public record ReglaDanioRequest(
        @Schema(description = "Vida actual (0 = eliminado)", example = "100", minimum = "0", maximum = "100") Integer vida,
        @Schema(description = "Chaleco actual", example = "100", minimum = "0", maximum = "100") Integer armadura,
        @Schema(description = "Daño que llega", example = "36", minimum = "0", maximum = "500") Integer danio) {
}
