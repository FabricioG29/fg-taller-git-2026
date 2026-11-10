package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import py.edu.uc.lp3exceptions.ValorInvalidoException;

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
        if (numeroPerdigones <= 0) {
            throw new ValorInvalidoException("Debe tener al menos un perdigón");
        }
        if (!Float.isFinite(distanciaEfectiva) || distanciaEfectiva <= 0f) {
            throw new ValorInvalidoException("La distancia efectiva debe ser > 0");
        }
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
