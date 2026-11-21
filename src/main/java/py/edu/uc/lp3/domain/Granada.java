package py.edu.uc.lp3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Granada: no tiene munición, se lanza y tiene cooldown.
 * detonar() es private: nadie de afuera puede detonarla sin lanzarla.
 * esLetal NO es un atributo: se deduce del daño (evita estados contradictorios
 * como "letal con daño 0").
 */
@Entity
@DiscriminatorValue("GRANADA")
public class Granada extends Arma {

    private float radioExplosion;   // metros
    private float tiempoActivacion; // segundos hasta detonar
    private String efecto;
    private long cooldownMs;
    private long ultimoLanzamientoMs = Long.MIN_VALUE;

    /** Solo para JPA. */
    protected Granada() {
    }

    public Granada(String nombre, int precio, Equipo equipo, int danio,
                   float radioExplosion, float tiempoActivacion, String efecto, long cooldownMs) {
        super(nombre, precio, equipo, danio);
        this.radioExplosion = radioExplosion;
        this.tiempoActivacion = tiempoActivacion;
        this.efecto = efecto.trim();
        this.cooldownMs = cooldownMs;
    }

    /** Para una granada, "usarse" es lanzarse. */
    @Override
    public ResultadoAccion usar() {
        return lanzar();
    }

    public ResultadoAccion lanzar() {
        long espera = esperaRestanteMs();
        if (espera > 0) {
            return new ResultadoAccion(getNombre(), getClass().getSimpleName(), "en cooldown", 0,
                    "Esperar " + espera + " ms");
        }
        ultimoLanzamientoMs = System.currentTimeMillis();
        return detonar();
    }

    /** Una granada no tiene cargador: "recargar" es esperar a que termine el cooldown. */
    @Override
    public ResultadoAccion recargar() {
        long espera = esperaRestanteMs();
        return new ResultadoAccion(getNombre(), getClass().getSimpleName(), "cooldown", 0,
                espera > 0 ? "Disponible en " + espera + " ms" : "Lista para lanzar");
    }

    private long esperaRestanteMs() {
        if (ultimoLanzamientoMs == Long.MIN_VALUE) {
            return 0;
        }
        long transcurrido = System.currentTimeMillis() - ultimoLanzamientoMs;
        return Math.max(0, cooldownMs - transcurrido);
    }

    private ResultadoAccion detonar() {
        return new ResultadoAccion(getNombre(), getClass().getSimpleName(), "explota", getDanioBase(),
                efecto + ", radio " + radioExplosion + " m, detona a los " + tiempoActivacion + " s"
                        + (esLetal() ? "" : " (no letal)"));
    }

    @Override
    public String inspeccionar() {
        long espera = esperaRestanteMs();
        return super.inspeccionar() + (espera > 0 ? " - en cooldown " + espera + " ms" : " - lista");
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

    public long getCooldownMs() {
        return cooldownMs;
    }

    @JsonProperty("letal")
    public boolean esLetal() {
        return getDanioBase() > 0;
    }
}
