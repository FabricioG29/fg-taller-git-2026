package py.edu.uc.lp3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * Clase base de todo el dominio CS2.
 * Concentra lo común (nombre, precio, equipo, daño base) y declara el contrato
 * {@link #usar()}: toda arma sabe "usarse", pero el padre no puede saber CÓMO
 * (un rifle dispara y gasta balas; una granada se lanza y tiene cooldown).
 *
 * Persistencia: toda la familia vive en UNA tabla "arma" (SINGLE_TABLE) con la
 * columna "tipo" como discriminador. En el JSON el subtipo sale en "tipo".
 * No hay setters: JPA usa el constructor vacío protected y accede a los campos.
 */
@Entity
@Table(name = "arma")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 20)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Pistola.class, name = "pistola"),
        @JsonSubTypes.Type(value = RifleAsalto.class, name = "rifle"),
        @JsonSubTypes.Type(value = Francotirador.class, name = "francotirador"),
        @JsonSubTypes.Type(value = Escopeta.class, name = "escopeta"),
        @JsonSubTypes.Type(value = Subfusil.class, name = "subfusil"),
        @JsonSubTypes.Type(value = Granada.class, name = "granada")
})
@JsonPropertyOrder({"id", "nombre", "precio", "equipo", "danio", "tienda"})
public abstract class Arma implements Inspeccionable {

    public static final int PRECIO_MAXIMO = 10_000;
    public static final int DANIO_MAXIMO = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    private int precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Equipo equipo;

    private int danio; // estaba repetido en ArmaDeFuego y Granada -> se generalizó acá

    /** Solo para JPA: no deja crear armas vacías desde afuera de la jerarquía. */
    protected Arma() {
    }

    protected Arma(String nombre, int precio, Equipo equipo, int danio) {
        if (equipo == null) {
            throw new ValorInvalidoException("El equipo es obligatorio");
        }
        if (danio < 0 || danio > DANIO_MAXIMO) {
            throw new ValorInvalidoException("Daño inválido: " + danio + " (0.." + DANIO_MAXIMO + ")");
        }
        this.nombre = validarNombre(nombre);
        this.precio = validarPrecio(precio);
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

    /** PATCH: cambiar el nombre pasa por la misma regla que el constructor. */
    public void renombrar(String nuevoNombre) {
        this.nombre = validarNombre(nuevoNombre);
    }

    /** PATCH: la tienda ajusta el precio, siempre dentro del rango legal. */
    public void cambiarPrecio(int nuevoPrecio) {
        this.precio = validarPrecio(nuevoPrecio);
    }

    /** Cómo aparece en la tienda. Igual para todas: no se repite en cada hija. */
    @JsonProperty("tienda")
    public final String mostrarEnTienda() {
        return nombre + " (" + getClass().getSimpleName() + ") - $" + precio + " - " + equipo;
    }

    /** Protocolo Inspeccionable. Las hijas lo extienden con su propio estado. */
    @Override
    public String inspeccionar() {
        return mostrarEnTienda();
    }

    /** ¿Este jugador puede usar el arma? Las de "AMBOS" las usa cualquiera. */
    public boolean esDelEquipo(Equipo equipoJugador) {
        return equipo == Equipo.AMBOS || equipo == equipoJugador;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("precio")
    public int obtenerPrecio() {
        return precio;
    }

    @JsonProperty("equipo")
    public Equipo obtenerEquipo() {
        return equipo;
    }

    /** Solo las hijas necesitan el daño base para su propio cálculo. En el JSON se ve como "danio". */
    @JsonProperty("danio")
    protected int getDanioBase() {
        return danio;
    }

    private static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValorInvalidoException("El nombre del arma no puede estar vacío");
        }
        if (nombre.trim().length() > 60) {
            throw new ValorInvalidoException("El nombre del arma no puede superar 60 caracteres");
        }
        return nombre.trim();
    }

    private static int validarPrecio(int precio) {
        if (precio < 0 || precio > PRECIO_MAXIMO) {
            throw new ValorInvalidoException("Precio inválido: " + precio + " (0.." + PRECIO_MAXIMO + ")");
        }
        return precio;
    }
}
