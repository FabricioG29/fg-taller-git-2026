package py.edu.uc.lp3.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Pregunta a la regla de munición: ¿alcanza el cargador para esta ráfaga? No toca la base. */
@Schema(description = "Estado del cargador y ráfaga que se quiere disparar")
public record ReglaMunicionRequest(
        @Schema(description = "Balas en el cargador", example = "3", minimum = "0", maximum = "200") Integer cargador,
        @Schema(description = "Balas que gasta cada disparo (1 si se omite; escopeta = 1 cartucho)",
                example = "1", minimum = "1", maximum = "10") Integer balasPorDisparo,
        @Schema(description = "Disparos de la ráfaga", example = "2", minimum = "1", maximum = "30") Integer disparos) {
}
