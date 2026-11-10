package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** Francotirador: con zoom activo ignora la precisión y suma penetración. */
@Entity
@DiscriminatorValue("FRANCOTIRADOR")
public class Francotirador extends ArmaDeFuego {

    private float zoom;        // aumento, ej. 4x
    private float penetracion; // 0..1, daño extra
    private boolean zoomActivo;

    /** Solo para JPA. */
    protected Francotirador() {
    }

    public Francotirador(String nombre, int precio, Equipo equipo, int danio,
                         float precision, int capacidadCargador, int municionReserva,
                         float zoom, float penetracion) {
        super(nombre, precio, equipo, danio, precision, capacidadCargador, municionReserva, 3.7f);
        if (!Float.isFinite(zoom) || zoom < 1f) {
            throw new ValorInvalidoException("El zoom debe ser >= 1");
        }
        if (!Float.isFinite(penetracion) || penetracion < 0f || penetracion > 1f) {
            throw new ValorInvalidoException("Penetración inválida (0..1)");
        }
        this.zoom = zoom;
        this.penetracion = penetracion;
    }

    public void activarZoom() {
        zoomActivo = true;
    }

    public void desactivarZoom() {
        zoomActivo = false;
    }

    public boolean isZoomActivo() {
        return zoomActivo;
    }

    public float getPenetracion() {
        return penetracion;
    }

    public float getZoom() {
        return zoom;
    }

    @Override
    protected int calcularDanio() {
        if (!zoomActivo) {
            return super.calcularDanio(); // sin mira: se comporta como cualquier arma
        }
        return Math.round(getDanioBase() * (1f + penetracion)); // reemplaza al padre
    }
}
