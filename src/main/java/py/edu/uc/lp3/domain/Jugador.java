package py.edu.uc.lp3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * Jugador de CS2: tiene vida y armadura (chaleco). Es la segunda clase
 * persistida que necesita la simulación: es quien dispara y quien recibe el daño.
 * Vida y armadura solo cambian por mensajes del dominio, nunca por setters.
 */
@Entity
@Table(name = "jugador")
@JsonPropertyOrder({"id", "nombre", "equipo", "vida", "armadura", "eliminado"})
public class Jugador implements Inspeccionable {

    public static final int VIDA_MAXIMA = 100;
    public static final int ARMADURA_MAXIMA = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Equipo equipo;

    private int vida;
    private int armadura;

    /** Solo para JPA. */
    protected Jugador() {
    }

    /** Constructor simple: el jugador arranca la ronda con vida y chaleco completos. */
    public Jugador(String nombre, Equipo equipo) {
        this(nombre, equipo, VIDA_MAXIMA, ARMADURA_MAXIMA);
    }

    /** Sobrecarga: jugador que ya viene herido o sin chaleco. */
    public Jugador(String nombre, Equipo equipo, int vida, int armadura) {
        if (nombre == null || nombre.isBlank() || nombre.trim().length() > 40) {
            throw new ValorInvalidoException("El nombre del jugador es obligatorio (1..40 caracteres)");
        }
        if (equipo == null || equipo == Equipo.AMBOS) {
            throw new ValorInvalidoException("Un jugador juega para TERRORISTAS o ANTITERRORISTAS");
        }
        if (vida < 1 || vida > VIDA_MAXIMA) {
            throw new ValorInvalidoException("Vida inválida: " + vida + " (1.." + VIDA_MAXIMA + ")");
        }
        if (armadura < 0 || armadura > ARMADURA_MAXIMA) {
            throw new ValorInvalidoException("Armadura inválida: " + armadura + " (0.." + ARMADURA_MAXIMA + ")");
        }
        this.nombre = nombre.trim();
        this.equipo = equipo;
        this.vida = vida;
        this.armadura = armadura;
    }

    @JsonProperty("eliminado")
    public boolean estaEliminado() {
        return vida == 0;
    }

    @Override
    public String inspeccionar() {
        return nombre + " [" + equipo + "] vida " + vida + "/" + VIDA_MAXIMA
                + ", armadura " + armadura + (estaEliminado() ? " - ELIMINADO" : "");
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public int getVida() {
        return vida;
    }

    public int getArmadura() {
        return armadura;
    }
}
