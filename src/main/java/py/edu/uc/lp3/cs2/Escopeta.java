package py.edu.uc.lp3.cs2;

/** Escopeta: el daño base es por perdigón; un disparo suelta todos. */
public class Escopeta extends ArmaDeFuego {

    private final int numeroPerdigones;
    private final float distanciaEfectiva; // metros

    public Escopeta(String nombre, int precio, Equipo equipo, int danioPorPerdigon,
                    float precision, int capacidadCargador, int municionReserva,
                    int numeroPerdigones, float distanciaEfectiva) {
        super(nombre, precio, equipo, danioPorPerdigon, precision, capacidadCargador, municionReserva, 0.5f);
        if (numeroPerdigones <= 0) {
            throw new IllegalArgumentException("Debe tener al menos un perdigón");
        }
        if (distanciaEfectiva <= 0f) {
            throw new IllegalArgumentException("La distancia efectiva debe ser > 0");
        }
        this.numeroPerdigones = numeroPerdigones;
        this.distanciaEfectiva = distanciaEfectiva;
    }

    public float getDistanciaEfectiva() {
        return distanciaEfectiva;
    }

    @Override
    protected int calcularDanio() {
        return super.calcularDanio() * numeroPerdigones; // extiende al padre
    }
}
