package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** Escopeta: el daño base es por perdigón; un disparo suelta todos. */
@Entity
@DiscriminatorValue("ESCOPETA")
public class Escopeta extends ArmaDeFuego {

    private int numeroPerdigones;
    private float distanciaEfectiva; // metros

    /** Solo para JPA. */
    protected Escopeta() {
    }

    public Escopeta(String nombre, int precio, Equipo equipo, int danioPorPerdigon,
                    float precision, int capacidadCargador, int municionReserva,
                    int numeroPerdigones, float distanciaEfectiva) {
        super(nombre, precio, equipo, danioPorPerdigon, precision, capacidadCargador, municionReserva, 0.5f);
        this.numeroPerdigones = numeroPerdigones;
        this.distanciaEfectiva = distanciaEfectiva;
    }

    public int getNumeroPerdigones() {
        return numeroPerdigones;
    }

    public float getDistanciaEfectiva() {
        return distanciaEfectiva;
    }

    @Override
    protected int calcularDanio() {
        return super.calcularDanio() * numeroPerdigones; // extiende al padre
    }
}
