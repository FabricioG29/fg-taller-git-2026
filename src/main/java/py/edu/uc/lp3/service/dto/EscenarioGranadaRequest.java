package py.edu.uc.lp3.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Pedido del escenario: quién lanza qué granada, a quién y a qué distancia. */
@Schema(description = "Lanzamiento de granada que deben aceptar A2 y A3 para guardarse")
public record EscenarioGranadaRequest(
        @Schema(description = "Jugador que lanza", example = "1") Long atacanteId,
        @Schema(description = "Jugador que recibe la explosión", example = "2") Long objetivoId,
        @Schema(description = "Id de una granada sin usar", example = "2") Long granadaId,
        @Schema(description = "Metros al centro de la explosión (lo validan A2 y A3)", example = "2.0") Double distancia) {
}
