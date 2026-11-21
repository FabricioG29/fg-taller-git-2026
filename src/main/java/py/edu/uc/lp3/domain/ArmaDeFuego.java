package py.edu.uc.lp3.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;


import java.util.ArrayList;
import java.util.List;

/**
 * Arma que dispara balas. Guarda la munición: nadie de afuera puede
 * cambiar el cargador ni la reserva, solo disparar() y recargar().
 * La regla de munición (no disparar con el cargador vacío) la aplica ReglasDeJuegoService
 * antes de llamar a disparar().
 * Las hijas especializan el daño y las balas por disparo con dos "ganchos"
 * protected; el control de munición NO se puede pisar (métodos final).
 */
@Entity
public abstract class ArmaDeFuego extends Arma {

    @Column(name = "precision_disparo")
    private float precision;          // 0.0 .. 1.0
    private int capacidadCargador;    // necesario para saber hasta dónde recargar
    private int municionCargador;
    private int municionReserva;
    private float tiempoRecarga;      // segundos

    /** Solo para JPA. */
    protected ArmaDeFuego() {
    }

    protected ArmaDeFuego(String nombre, int precio, Equipo equipo, int danio,
                          float precision, int capacidadCargador, int municionReserva,
                          float tiempoRecarga) {
        super(nombre, precio, equipo, danio);
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

    /** Sin balas en el cargador el arma no está lista: el service lo rechaza (SinMunicionException). */
    @Override
    public EstadoUso estadoDeUso() {
        return municionCargador == 0 ? EstadoUso.SIN_MUNICION : EstadoUso.LISTA;
    }

    /** Gasta las balas de un disparo. Con el cargador vacío solo hace "click" y el estado no cambia. */
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

    /** Sobrecarga del mensaje: varios disparos seguidos. Reutiliza disparar(); el service valida "veces". */
    public final List<ResultadoAccion> disparar(int veces) {
        List<ResultadoAccion> resultados = new ArrayList<>();
        for (int i = 0; i < veces; i++) {
            resultados.add(disparar());
        }
        return resultados;
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

    @Override
    public String inspeccionar() {
        return super.inspeccionar() + " - munición " + municionCargador + "/" + municionReserva;
    }

    public float getPrecision() {
        return precision;
    }

    public int getCapacidadCargador() {
        return capacidadCargador;
    }

    public int getMunicionCargador() {
        return municionCargador;
    }

    public int getMunicionReserva() {
        return municionReserva;
    }
}
