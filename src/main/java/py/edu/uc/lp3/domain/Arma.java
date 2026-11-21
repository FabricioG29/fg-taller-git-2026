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

/**
 * Clase base de todo el dominio CS2.
 * Concentra lo común (nombre, precio, equipo, daño base) y declara el contrato
 * {@link #usar()}: toda arma sabe "usarse", pero el padre no puede saber CÓMO
 * (un rifle dispara y gasta balas; una granada se lanza una sola vez).
 *
 * Persistencia: toda la familia vive en UNA tabla "arma" (SINGLE_TABLE) con la
 * columna "tipo" como discriminador. En el JSON el subtipo sale en "tipo".
 * No hay setters: JPA usa el constructor vacío protected y accede a los campos.
 * La entidad solo guarda su estado: las validaciones, las reglas y las
 * excepciones viven en la capa service (ArmaService, ReglasDeJuegoService).
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
        this.nombre = nombre;
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

    /** ¿Se puede usar ahora? Por defecto sí; ArmaDeFuego y Granada lo redefinen. */
    public EstadoUso estadoDeUso() {
        return EstadoUso.LISTA;
    }

    /** PATCH: ArmaService ya validó el nombre. */
    public void renombrar(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }

    /** PATCH: ArmaService ya validó el rango del precio. */
    public void cambiarPrecio(int nuevoPrecio) {
        this.precio = nuevoPrecio;
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
}
