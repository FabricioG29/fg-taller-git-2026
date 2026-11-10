package py.edu.uc.lp3.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * Registro persistido de un disparo de la simulación (tercera clase persistida).
 * Guarda quién disparó a quién, con qué arma y cómo se repartió el daño.
 * Es inmutable: una vez registrado, la historia no se edita.
 */
@Entity
@Table(name = "disparo")
@JsonPropertyOrder({"id", "momento", "atacanteId", "atacante", "objetivoId", "objetivo", "armaId", "arma",
        "accion", "danioArma", "absorbidoPorArmadura", "danioVida", "vidaRestante", "armaduraRestante",
        "eliminado", "detalle"})
public class Disparo implements Inspeccionable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime momento;

    private Long atacanteId;
    @Column(length = 40)
    private String atacante;

    private Long objetivoId;
    @Column(length = 40)
    private String objetivo;

    private Long armaId;
    @Column(length = 60)
    private String arma;

    @Column(length = 20)
    private String accion;
    private int danioArma;
    private int absorbidoPorArmadura;
    private int danioVida;
    private int vidaRestante;
    private int armaduraRestante;
    private boolean eliminado;
    @Column(length = 200)
    private String detalle;

    /** Solo para JPA. */
    protected Disparo() {
    }

    public Disparo(Jugador atacante, Jugador objetivo, Arma arma, ResultadoAccion resultado, Impacto impacto) {
        if (atacante == null || objetivo == null || arma == null || resultado == null || impacto == null) {
            throw new ValorInvalidoException("Un disparo necesita atacante, objetivo, arma, resultado e impacto");
        }
        this.momento = LocalDateTime.now();
        this.atacanteId = atacante.getId();
        this.atacante = atacante.getNombre();
        this.objetivoId = objetivo.getId();
        this.objetivo = objetivo.getNombre();
        this.armaId = arma.getId();
        this.arma = arma.getNombre();
        this.accion = resultado.accion();
        this.detalle = resultado.detalle();
        this.danioArma = impacto.danioRecibido();
        this.absorbidoPorArmadura = impacto.absorbidoPorArmadura();
        this.danioVida = impacto.danioVida();
        this.vidaRestante = impacto.vidaRestante();
        this.armaduraRestante = impacto.armaduraRestante();
        this.eliminado = impacto.eliminado();
    }

    @Override
    public String inspeccionar() {
        return "#" + id + " " + atacante + " -> " + objetivo + " con " + arma + ": " + danioArma
                + " de daño (" + absorbidoPorArmadura + " al chaleco, " + danioVida + " a la vida)"
                + (eliminado ? " - ELIMINADO" : "");
    }

    public Long getId() { return id; }
    public LocalDateTime getMomento() { return momento; }
    public Long getAtacanteId() { return atacanteId; }
    public String getAtacante() { return atacante; }
    public Long getObjetivoId() { return objetivoId; }
    public String getObjetivo() { return objetivo; }
    public Long getArmaId() { return armaId; }
    public String getArma() { return arma; }
    public String getAccion() { return accion; }
    public int getDanioArma() { return danioArma; }
    public int getAbsorbidoPorArmadura() { return absorbidoPorArmadura; }
    public int getDanioVida() { return danioVida; }
    public int getVidaRestante() { return vidaRestante; }
    public int getArmaduraRestante() { return armaduraRestante; }
    public boolean isEliminado() { return eliminado; }
    public String getDetalle() { return detalle; }
}
