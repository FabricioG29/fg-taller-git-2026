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

import py.edu.uc.lp3exceptions.JugadorEliminadoException;
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

    /**
     * REGLA 2 (daño con chaleco): la armadura absorbe la mitad del daño (hasta lo
     * que le queda) y se gasta en esa cantidad; el resto baja la vida, que nunca
     * queda negativa. Con vida 0 el jugador queda eliminado.
     */
    public Impacto recibirDanio(int danio) {
        if (danio < 0) {
            throw new ValorInvalidoException("El daño no puede ser negativo: " + danio);
        }
        verificarQuePuedeRecibirDanio();
        int absorbido = Math.min(armadura, danio / 2);
        armadura -= absorbido;
        int aLaVida = Math.min(vida, danio - absorbido);
        vida -= aLaVida;
        return new Impacto(danio, absorbido, aLaVida, vida, armadura, estaEliminado());
    }

    /** Antes de disparar: tiene que seguir vivo y el arma tiene que ser de su bando (o de AMBOS). */
    public void verificarQuePuedeDisparar(Arma arma) {
        if (estaEliminado()) {
            throw new JugadorEliminadoException(nombre + " está eliminado y no puede disparar");
        }
        if (arma == null || !arma.esDelEquipo(equipo)) {
            throw new ValorInvalidoException((arma == null ? "El arma" : arma.getNombre())
                    + " no es de " + equipo + ": " + nombre + " no la puede usar");
        }
    }

    public void verificarQuePuedeRecibirDanio() {
        if (estaEliminado()) {
            throw new JugadorEliminadoException(nombre + " ya está eliminado: no puede recibir más daño");
        }
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
