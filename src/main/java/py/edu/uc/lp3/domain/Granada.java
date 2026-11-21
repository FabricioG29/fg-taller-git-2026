package py.edu.uc.lp3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Granada: un consumible de un solo uso. Se lanza, explota y queda usada; no
 * tiene munición, ni recarga, ni tiempo de enfriamiento (como en CS2).
 * Un segundo lanzamiento lo rechaza ReglasDeJuegoService con GranadaUsadaException (409).
 * detonar() es private: nadie de afuera puede detonarla sin lanzarla.
 * esLetal NO es un atributo: se deduce del daño.
 */
@Entity
@DiscriminatorValue("GRANADA")
public class Granada extends Arma {

    private float radioExplosion;   // metros
    private float tiempoActivacion; // segundos hasta detonar
    private String efecto;
    private boolean usada;

    /** Solo para JPA. */
    protected Granada() {
    }

    public Granada(String nombre, int precio, Equipo equipo, int danio,
                   float radioExplosion, float tiempoActivacion, String efecto) {
        super(nombre, precio, equipo, danio);
        this.radioExplosion = radioExplosion;
        this.tiempoActivacion = tiempoActivacion;
        this.efecto = efecto;
        this.usada = false;
    }

    /** Para una granada, "usarse" es lanzarse. */
    @Override
    public ResultadoAccion usar() {
        return lanzar();
    }

    /** Lanzarla la gasta. Si ya estaba usada no explota de nuevo (el service lo rechaza antes). */
    public ResultadoAccion lanzar() {
        if (usada) {
            return new ResultadoAccion(getNombre(), getClass().getSimpleName(), "ya usada", 0,
                    "La granada ya explotó; es de un solo uso");
        }
        usada = true;
        return detonar();
    }

    /** Una granada usada ya no se puede usar: no se recarga ni se enfría. */
    @Override
    public EstadoUso estadoDeUso() {
        return usada ? EstadoUso.USADA : EstadoUso.LISTA;
    }

    private ResultadoAccion detonar() {
        return new ResultadoAccion(getNombre(), getClass().getSimpleName(), "explota", getDanioBase(),
                efecto + ", radio " + radioExplosion + " m, detona a los " + tiempoActivacion + " s"
                        + (esLetal() ? "" : " (no letal)"));
    }

    @Override
    public String inspeccionar() {
        return super.inspeccionar() + (usada ? " - usada" : " - sin usar");
    }

    public float getRadioExplosion() {
        return radioExplosion;
    }

    public float getTiempoActivacion() {
        return tiempoActivacion;
    }

    public String getEfecto() {
        return efecto;
    }

    public boolean isUsada() {
        return usada;
    }

    @JsonProperty("letal")
    public boolean esLetal() {
        return getDanioBase() > 0;
    }
}
