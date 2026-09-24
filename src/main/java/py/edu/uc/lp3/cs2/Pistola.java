package py.edu.uc.lp3.cs2;

/** Pistola: puede alternar a modo ráfaga (3 balas por disparo). */
public class Pistola extends ArmaDeFuego {

    private boolean modoRafaga;

    public Pistola(String nombre, int precio, Equipo equipo, int danio,
                   float precision, int capacidadCargador, int municionReserva) {
        super(nombre, precio, equipo, danio, precision, capacidadCargador, municionReserva, 2.2f);
    }

    /** Alterna el modo ráfaga (activa/desactiva). */
    public void activarModoRafaga() {
        modoRafaga = !modoRafaga;
    }

    public boolean isModoRafaga() {
        return modoRafaga;
    }

    @Override
    protected int balasPorDisparo() {
        return modoRafaga ? 3 : 1; // reemplaza al padre
    }

    @Override
    protected int calcularDanio() {
        // llama al padre y agrega su regla: la ráfaga pierde 20 % de daño por bala
        int base = super.calcularDanio();
        return modoRafaga ? Math.round(base * 0.8f) : base;
    }
}
