package py.edu.uc.lp3.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** Rifle de asalto: el modo de disparo decide las balas por disparo. */
@Entity
@DiscriminatorValue("RIFLE")
public class RifleAsalto extends ArmaDeFuego {

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private ModoDisparo modoDisparo = ModoDisparo.AUTOMATICO;
    private float cadenciaDisparo; // disparos por segundo

    /** Solo para JPA. */
    protected RifleAsalto() {
    }

    public RifleAsalto(String nombre, int precio, Equipo equipo, int danio,
                       float precision, int capacidadCargador, int municionReserva,
                       float cadenciaDisparo) {
        super(nombre, precio, equipo, danio, precision, capacidadCargador, municionReserva, 2.5f);
        if (!Float.isFinite(cadenciaDisparo) || cadenciaDisparo <= 0f) {
            throw new ValorInvalidoException("La cadencia debe ser > 0");
        }
        this.cadenciaDisparo = cadenciaDisparo;
    }

    public void cambiarModo(ModoDisparo m) {
        if (m == null) {
            throw new ValorInvalidoException("El modo de disparo no puede ser nulo");
        }
        this.modoDisparo = m;
    }

    public ModoDisparo getModoDisparo() {
        return modoDisparo;
    }

    public float getCadenciaDisparo() {
        return cadenciaDisparo;
    }

    @Override
    protected int balasPorDisparo() {
        return modoDisparo.balasPorDisparo(); // el enum sabe, sin switch
    }
}
