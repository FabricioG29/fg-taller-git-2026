package py.edu.uc.lp3.rest.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.Escopeta;
import py.edu.uc.lp3.domain.Francotirador;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.domain.RifleAsalto;
import py.edu.uc.lp3.domain.Subfusil;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * Cuerpo del POST /api/armas. El campo "tipo" dice qué hija se crea
 * (Jackson elige el record por ese nombre). Cada record solo traduce JSON a
 * un constructor del dominio: las reglas numéricas las valida el constructor,
 * así un JSON ilegal nunca llega a la base.
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

    Arma crear();

    /** Sin precision/cargador/reserva usa el constructor simple; con los tres, el sobrecargado. */
    record PistolaRequest(String nombre, Integer precio, Equipo equipo, Integer danio,
                          Float precision, Integer cargador, Integer reserva) implements CrearArmaRequest {
        @Override
        public Arma crear() {
            if (precision == null && cargador == null && reserva == null) {
                return new Pistola(nombre, req(precio, "precio"), equipo, req(danio, "danio"));
            }
            return new Pistola(nombre, req(precio, "precio"), equipo, req(danio, "danio"),
                    req(precision, "precision"), req(cargador, "cargador"), req(reserva, "reserva"));
        }
    }

    record RifleRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float precision,
                        Integer cargador, Integer reserva, Float cadencia) implements CrearArmaRequest {
        @Override
        public Arma crear() {
            return new RifleAsalto(nombre, req(precio, "precio"), equipo, req(danio, "danio"),
                    req(precision, "precision"), req(cargador, "cargador"), req(reserva, "reserva"),
                    req(cadencia, "cadencia"));
        }
    }

    record FrancotiradorRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float precision,
                                Integer cargador, Integer reserva, Float zoom, Float penetracion)
            implements CrearArmaRequest {
        @Override
        public Arma crear() {
            return new Francotirador(nombre, req(precio, "precio"), equipo, req(danio, "danio"),
                    req(precision, "precision"), req(cargador, "cargador"), req(reserva, "reserva"),
                    req(zoom, "zoom"), req(penetracion, "penetracion"));
        }
    }

    record EscopetaRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float precision,
                           Integer cargador, Integer reserva, Integer perdigones, Float distanciaEfectiva)
            implements CrearArmaRequest {
        @Override
        public Arma crear() {
            return new Escopeta(nombre, req(precio, "precio"), equipo, req(danio, "danio"),
                    req(precision, "precision"), req(cargador, "cargador"), req(reserva, "reserva"),
                    req(perdigones, "perdigones"), req(distanciaEfectiva, "distanciaEfectiva"));
        }
    }

    record SubfusilRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float precision,
                           Integer cargador, Integer reserva, Float controlRetroceso) implements CrearArmaRequest {
        @Override
        public Arma crear() {
            return new Subfusil(nombre, req(precio, "precio"), equipo, req(danio, "danio"),
                    req(precision, "precision"), req(cargador, "cargador"), req(reserva, "reserva"),
                    req(controlRetroceso, "controlRetroceso"));
        }
    }

    record GranadaRequest(String nombre, Integer precio, Equipo equipo, Integer danio, Float radioExplosion,
                          Float tiempoActivacion, String efecto, Long cooldownMs) implements CrearArmaRequest {
        @Override
        public Arma crear() {
            return new Granada(nombre, req(precio, "precio"), equipo, req(danio, "danio"),
                    req(radioExplosion, "radioExplosion"), req(tiempoActivacion, "tiempoActivacion"),
                    efecto, req(cooldownMs, "cooldownMs"));
        }
    }

    /** Un campo obligatorio que falta es un valor inválido, no un NullPointerException. */
    private static <T> T req(T valor, String campo) {
        if (valor == null) {
            throw new ValorInvalidoException("Falta el campo obligatorio \"" + campo + "\"");
        }
        return valor;
    }
}
