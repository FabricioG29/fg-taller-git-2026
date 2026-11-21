package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** Subfusil: dispara de a 2 y el retroceso le quita daño según el control. */
@Entity
@DiscriminatorValue("SUBFUSIL")
public class Subfusil extends ArmaDeFuego {

    private float controlRetroceso; // 0..1 (1 = sin retroceso)

    /** Solo para JPA. */
    protected Subfusil() {
    }

    public Subfusil(String nombre, int precio, Equipo equipo, int danio,
                    float precision, int capacidadCargador, int municionReserva,
                    float controlRetroceso) {
        super(nombre, precio, equipo, danio, precision, capacidadCargador, municionReserva, 2.1f);
        this.controlRetroceso = controlRetroceso;
    }

    public float getControlRetroceso() {
        return controlRetroceso;
    }

    @Override
    protected int balasPorDisparo() {
        return 2;
    }

    @Override
    protected int calcularDanio() {
        return Math.round(super.calcularDanio() * (0.5f + controlRetroceso / 2f));
    }
}
