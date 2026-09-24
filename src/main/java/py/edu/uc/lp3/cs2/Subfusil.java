package py.edu.uc.lp3.cs2;

/** Subfusil: dispara de a 2 y el retroceso le quita daño según el control. */
public class Subfusil extends ArmaDeFuego {

    private final float controlRetroceso; // 0..1 (1 = sin retroceso)

    public Subfusil(String nombre, int precio, Equipo equipo, int danio,
                    float precision, int capacidadCargador, int municionReserva,
                    float controlRetroceso) {
        super(nombre, precio, equipo, danio, precision, capacidadCargador, municionReserva, 2.1f);
        if (controlRetroceso < 0f || controlRetroceso > 1f) {
            throw new IllegalArgumentException("Control de retroceso inválido (0..1)");
        }
        this.controlRetroceso = controlRetroceso;
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
