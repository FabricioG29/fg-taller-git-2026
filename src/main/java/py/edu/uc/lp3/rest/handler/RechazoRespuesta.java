package py.edu.uc.lp3.rest.handler;

import io.swagger.v3.oas.annotations.media.Schema;

/** Error de un compañero: además del formato común, dice quién fue y qué contestó. */
@Schema(description = "Rechazo o falta de respuesta de A2 o A3. 'rol' dice a quién atribuirlo; la base no cambió.")
public record RechazoRespuesta(
        @Schema(example = "409") int status,
        @Schema(example = "RECHAZO_A3", allowableValues = {"RECHAZO_A2", "RECHAZO_A3",
                "COMPANERO_NO_DISPONIBLE_A2", "COMPANERO_NO_DISPONIBLE_A3"}) String error,
        @Schema(example = "RechazoDeCompaneroException") String excepcion,
        @Schema(example = "A3 (Diana Aquino) rechazó la jugada con HTTP 422: no se guardó nada") String mensaje,
        @Schema(description = "A2 o A3", example = "A3") String rol,
        @Schema(example = "Diana Aquino") String companero,
        @Schema(example = "http://localhost:8082/api/armas/granadas/simular?equipoAtacante=Terrorista&equipoObjetivo=Terrorista&distancia=2.0")
        String urlConsultada,
        @Schema(description = "Código que devolvió el compañero (null si no respondió)", example = "422")
        Integer statusCompanero,
        @Schema(description = "Cuerpo que devolvió el compañero (recortado)") String respuestaCompanero,
        @Schema(description = "Siempre SIN_CAMBIOS: la jugada no se guardó", example = "SIN_CAMBIOS")
        String estadoH2) {
}
