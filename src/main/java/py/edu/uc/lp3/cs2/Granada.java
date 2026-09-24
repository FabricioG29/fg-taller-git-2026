package py.edu.uc.lp3.cs2;

/**
 * Granada: no tiene munición, se lanza y tiene cooldown.
 * detonar() es private: nadie de afuera puede detonarla sin lanzarla.
 * esLetal NO es un atributo: se deduce del daño (evita estados contradictorios
 * como "letal con daño 0").
 */
public class Granada extends Arma {

    private final float radioExplosion;   // metros
    private final float tiempoActivacion; // segundos hasta detonar
    private final String efecto;
    private final long cooldownMs;
    private long ultimoLanzamientoMs = Long.MIN_VALUE;

    public Granada(String nombre, int precio, Equipo equipo, int danio,
                   float radioExplosion, float tiempoActivacion, String efecto, long cooldownMs) {
        super(nombre, precio, equipo, danio);
        if (radioExplosion <= 0f) {
            throw new IllegalArgumentException("El radio de explosión debe ser > 0");
        }
        if (tiempoActivacion < 0f) {
            throw new IllegalArgumentException("El tiempo de activación no puede ser negativo");
        }
        if (efecto == null || efecto.isBlank()) {
            throw new IllegalArgumentException("La granada debe tener un efecto");
        }
        if (cooldownMs < 0) {
            throw new IllegalArgumentException("El cooldown no puede ser negativo");
        }
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

    public boolean esLetal() {
        return getDanioBase() > 0;
    }
}
