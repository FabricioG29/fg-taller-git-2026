package py.edu.uc.lp3.rest.handler;

import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import py.edu.uc.lp3exceptions.GranadaUsadaException;
import py.edu.uc.lp3exceptions.JuegoException;
import py.edu.uc.lp3exceptions.JugadorEliminadoException;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3exceptions.SinMunicionException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * Un handler por cada tipo de la jerarquía: la capa service lanza, acá solo se
 * traduce a JSON y código HTTP. El cliente distingue a las hermanas por el
 * código HTTP y por el campo "error".
 */
@RestControllerAdvice
public class ManejoErrores {

    private static final MediaType JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

    /** 400: el objeto quedaría en un estado imposible (invariante de construcción o de un mensaje). */
    @ExceptionHandler(ValorInvalidoException.class)
    public ResponseEntity<ErrorRespuesta> valorInvalido(ValorInvalidoException e) {
        return responder(HttpStatus.BAD_REQUEST, e);
    }

    /** 404: el id de la URL no existe en la base. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> noEncontrado(RecursoNoEncontradoException e) {
        return responder(HttpStatus.NOT_FOUND, e);
    }

    /** 409: el arma existe y el pedido es válido, pero su estado (cargador vacío) no lo permite. */
    @ExceptionHandler(SinMunicionException.class)
    public ResponseEntity<ErrorRespuesta> sinMunicion(SinMunicionException e) {
        return responder(HttpStatus.CONFLICT, e);
    }

    /** 409: la granada ya se lanzó; es un consumible de un solo uso. */
    @ExceptionHandler(GranadaUsadaException.class)
    public ResponseEntity<ErrorRespuesta> granadaUsada(GranadaUsadaException e) {
        return responder(HttpStatus.CONFLICT, e);
    }

    /** 422: el pedido es correcto, pero el jugador ya está eliminado y no puede participar. */
    @ExceptionHandler(JugadorEliminadoException.class)
    public ResponseEntity<ErrorRespuesta> jugadorEliminado(JugadorEliminadoException e) {
        return responder(HttpStatus.UNPROCESSABLE_ENTITY, e);
    }

    /** 400: JSON mal formado, "tipo" desconocido o un enum inexistente (no llega al dominio). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> jsonIlegible(HttpMessageNotReadableException e) {
        ErrorRespuesta cuerpo = new ErrorRespuesta(HttpStatus.BAD_REQUEST.value(), "JSON_INVALIDO",
                "HttpMessageNotReadableException",
                "El cuerpo no se puede leer: revisá el JSON, el campo \"tipo\" y el \"equipo\"");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(JSON_UTF8).body(cuerpo);
    }

    private ResponseEntity<ErrorRespuesta> responder(HttpStatus status, JuegoException e) {
        ErrorRespuesta cuerpo = new ErrorRespuesta(status.value(), e.getCodigo(),
                e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity.status(status).contentType(JSON_UTF8).body(cuerpo);
    }
}
