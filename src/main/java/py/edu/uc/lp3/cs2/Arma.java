package py.edu.uc.lp3.cs2;

/**
 * Clase base de todo el dominio CS2.
 * Concentra lo común (nombre, precio, equipo, daño base) y declara el contrato
 * {@link #usar()}: toda arma sabe "usarse", pero el padre no puede saber CÓMO
 * (un rifle dispara y gasta balas; una granada se lanza y tiene cooldown).
 */
public abstract class Arma {

    private final String nombre;
    private final int precio;
    private final Equipo equipo;
    private final int danio; // estaba repetido en ArmaDeFuego y Granada -> se generalizó acá

    protected Arma(String nombre, int precio, Equipo equipo, int danio) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del arma no puede estar vacío");
        }
        if (precio < 0 || precio > 10_000) {
            throw new IllegalArgumentException("Precio inválido: " + precio + " (0..10000)");
        }
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo es obligatorio");
        }
        if (danio < 0 || danio > 500) {
            throw new IllegalArgumentException("Daño inválido: " + danio + " (0..500)");
        }
        this.nombre = nombre.trim();
        this.precio = precio;
        this.equipo = equipo;
        this.danio = danio;
    }

    /** Mensaje común a todas las armas. Cada hija decide cómo se realiza. */
    public abstract ResultadoAccion usar();

    /**
     * Por defecto un arma no se recarga (ej. un cuchillo). Las que sí,
     * lo redefinen. Así el inventario puede pedir recargar() a cualquiera sin if.
     */
    public ResultadoAccion recargar() {
        return new ResultadoAccion(nombre, getClass().getSimpleName(), "sin recarga", 0,
                "Esta arma no usa munición");
    }

    /** Cómo aparece en la tienda. Igual para todas: no se repite en cada hija. */
    public final String mostrarEnTienda() {
        return nombre + " (" + getClass().getSimpleName() + ") - $" + precio + " - " + equipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int obtenerPrecio() {
        return precio;
    }

    public Equipo obtenerEquipo() {
        return equipo;
    }

    /** Solo las hijas necesitan el daño base para su propio cálculo. */
    protected int getDanioBase() {
        return danio;
    }
}
