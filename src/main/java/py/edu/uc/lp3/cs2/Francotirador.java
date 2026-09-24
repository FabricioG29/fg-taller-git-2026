package py.edu.uc.lp3.cs2;

/** Francotirador: con zoom activo ignora la precisión y suma penetración. */
public class Francotirador extends ArmaDeFuego {

    private final float zoom;        // aumento, ej. 4x
    private final float penetracion; // 0..1, daño extra
    private boolean zoomActivo;

    public Francotirador(String nombre, int precio, Equipo equipo, int danio,
                         float precision, int capacidadCargador, int municionReserva,
                         float zoom, float penetracion) {
        super(nombre, precio, equipo, danio, precision, capacidadCargador, municionReserva, 3.7f);
        if (zoom < 1f) {
            throw new IllegalArgumentException("El zoom debe ser >= 1");
        }
        if (penetracion < 0f || penetracion > 1f) {
            throw new IllegalArgumentException("Penetración inválida (0..1)");
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
