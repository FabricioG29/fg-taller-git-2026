package py.edu.uc.lp3.cs2;

/**
 * Arma que dispara balas. Controla la munición: nadie de afuera puede
 * cambiar el cargador ni la reserva, solo disparar() y recargar().
 * Las hijas especializan el daño y las balas por disparo con dos "ganchos"
 * protected; el control de munición NO se puede pisar (métodos final).
 */
public abstract class ArmaDeFuego extends Arma {

    private final float precision;          // 0.0 .. 1.0
    private final int capacidadCargador;    // necesario para saber hasta dónde recargar
    private int municionCargador;
    private int municionReserva;
    private final float tiempoRecarga;      // segundos

    protected ArmaDeFuego(String nombre, int precio, Equipo equipo, int danio,
                          float precision, int capacidadCargador, int municionReserva,
                          float tiempoRecarga) {
        super(nombre, precio, equipo, danio);
        if (danio <= 0) {
            throw new IllegalArgumentException("Un arma de fuego debe hacer daño (> 0)");
        }
        if (precision <= 0f || precision > 1f) {
            throw new IllegalArgumentException("Precisión inválida: " + precision + " (0..1]");
        }
        if (capacidadCargador <= 0) {
            throw new IllegalArgumentException("La capacidad del cargador debe ser > 0");
        }
        if (municionReserva < 0) {
            throw new IllegalArgumentException("La munición de reserva no puede ser negativa");
        }
        if (tiempoRecarga <= 0f) {
            throw new IllegalArgumentException("El tiempo de recarga debe ser > 0");
        }
        this.precision = precision;
        this.capacidadCargador = capacidadCargador;
        this.municionCargador = capacidadCargador; // sale de la tienda con el cargador lleno
        this.municionReserva = municionReserva;
        this.tiempoRecarga = tiempoRecarga;
    }

    /** Para un arma de fuego, "usarse" es disparar. */
    @Override
    public ResultadoAccion usar() {
        return disparar();
    }

    public final ResultadoAccion disparar() {
        if (municionCargador == 0) {
            return new ResultadoAccion(getNombre(), getClass().getSimpleName(), "click", 0,
                    "Cargador vacío, hay que recargar");
        }
        int balas = Math.min(balasPorDisparo(), municionCargador);
        municionCargador -= balas;
        int danioTotal = calcularDanio() * balas;
        return new ResultadoAccion(getNombre(), getClass().getSimpleName(), "dispara", danioTotal,
                balas + " bala(s), quedan " + municionCargador + "/" + municionReserva);
    }

    @Override
    public final ResultadoAccion recargar() {
        int faltan = capacidadCargador - municionCargador;
        int cargadas = Math.min(faltan, municionReserva);
        municionCargador += cargadas;
        municionReserva -= cargadas;
        return new ResultadoAccion(getNombre(), getClass().getSimpleName(), "recarga", 0,
                "+" + cargadas + " en " + tiempoRecarga + " s, quedan "
                        + municionCargador + "/" + municionReserva);
    }

    /** Gancho: daño de UNA bala. Por defecto, daño base afectado por la precisión. */
    protected int calcularDanio() {
        return Math.round(getDanioBase() * precision);
    }

    /** Gancho: cuántas balas salen por disparo. Por defecto una. */
    protected int balasPorDisparo() {
        return 1;
    }

    public int getMunicionCargador() {
        return municionCargador;
    }

    public int getMunicionReserva() {
        return municionReserva;
    }
}
