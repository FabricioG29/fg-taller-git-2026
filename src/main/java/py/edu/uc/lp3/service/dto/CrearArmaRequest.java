package py.edu.uc.lp3.service.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import py.edu.uc.lp3.domain.Equipo;

/**
 * Cuerpo del POST /api/armas. El campo "tipo" dice qué hija se crea
 * (Jackson elige el record por ese nombre). Los records solo transportan datos:
 * ArmaService valida cada campo y recién entonces llama al constructor, así un
 * JSON ilegal nunca llega a la base.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CrearArmaRequest.PistolaRequest.class, name = "pistola"),
        @JsonSubTypes.Type(value = CrearArmaRequest.RifleRequest.class, name = "rifle"),
        @JsonSubTypes.Type(value = CrearArmaRequest.FrancotiradorRequest.class, name = "francotirador"),
        @JsonSubTypes.Type(value = CrearArmaRequest.EscopetaRequest.class, name = "escopeta"),
        @JsonSubTypes.Type(value = CrearArmaRequest.SubfusilRequest.class, name = "subfusil"),
        @JsonSubTypes.Type(value = CrearArmaRequest.GranadaRequest.class, name = "granada")
})
public sealed interface CrearArmaRequest {

    /** Sin precision/cargador/reserva se usa el constructor simple; con los tres, el sobrecargado. */
    record PistolaRequest(String nombre, Integer precio, Equipo equipo, Integer danio,
                          Float precision, Integer cargador, Integer reserva) implements CrearArmaRequest {
    }

    record RifleRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float precision,
                        Integer cargador, Integer reserva, Float cadencia) implements CrearArmaRequest {
    }

    record FrancotiradorRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float precision,
                                Integer cargador, Integer reserva, Float zoom, Float penetracion)
            implements CrearArmaRequest {
    }

    record EscopetaRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float precision,
                           Integer cargador, Integer reserva, Integer perdigones, Float distanciaEfectiva)
            implements CrearArmaRequest {
    }

    record SubfusilRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float precision,
                           Integer cargador, Integer reserva, Float controlRetroceso) implements CrearArmaRequest {
    }

    record GranadaRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float radioExplosion,
                          Float tiempoActivacion, String efecto) implements CrearArmaRequest {
    }
}
