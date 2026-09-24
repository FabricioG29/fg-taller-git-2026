package py.edu.uc.lp3.cs2;

/** Rifle de asalto: el modo de disparo decide las balas por disparo. */
public class RifleAsalto extends ArmaDeFuego {

    private ModoDisparo modoDisparo = ModoDisparo.AUTOMATICO;
    private final float cadenciaDisparo; // disparos por segundo

    public RifleAsalto(String nombre, int precio, Equipo equipo, int danio,
                       float precision, int capacidadCargador, int municionReserva,
                       float cadenciaDisparo) {
        super(nombre, precio, equipo, danio, precision, capacidadCargador, municionReserva, 2.5f);
        if (cadenciaDisparo <= 0f) {
            throw new IllegalArgumentException("La cadencia debe ser > 0");
        }
        this.cadenciaDisparo = cadenciaDisparo;
    }

    public void cambiarModo(ModoDisparo m) {
        if (m == null) {
            throw new IllegalArgumentException("El modo de disparo no puede ser nulo");
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
