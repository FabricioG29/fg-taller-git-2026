package py.edu.uc.lp3.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Jugada del escenario cruzado (POO-09): un lanzamiento de granada que A2 y A3
 * aceptaron. Solo existe si los dos dijeron que sí; es inmutable.
 */
@Entity
@Table(name = "jugada")
@JsonPropertyOrder({"id", "momento", "atacanteId", "atacante", "objetivoId", "objetivo", "granadaId", "granada",
        "distancia", "factorDistanciaA2", "danioExplosion", "absorbidoPorArmadura", "danioVida", "vidaRestante",
        "armaduraRestante", "eliminado", "respuestaA2", "respuestaA3"})
public class Jugada implements Inspeccionable {

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
    private Long granadaId;
    @Column(length = 60)
    private String granada;
    private double distancia;
    private double factorDistanciaA2;
    private int danioExplosion;
    private int absorbidoPorArmadura;
    private int danioVida;
    private int vidaRestante;
    private int armaduraRestante;
    private boolean eliminado;
    @Column(length = 600)
    private String respuestaA2;
    @Column(length = 600)
    private String respuestaA3;

    /** Solo para JPA. */
    protected Jugada() {
    }

    public Jugada(Jugador atacante, Jugador objetivo, Granada granada, double distancia, double factorDistanciaA2,
                  Impacto impacto, String respuestaA2, String respuestaA3) {
        this.momento = LocalDateTime.now();
        this.atacanteId = atacante.getId();
        this.atacante = atacante.getNombre();
        this.objetivoId = objetivo.getId();
        this.objetivo = objetivo.getNombre();
        this.granadaId = granada.getId();
        this.granada = granada.getNombre();
        this.distancia = distancia;
        this.factorDistanciaA2 = factorDistanciaA2;
        this.danioExplosion = impacto.danioRecibido();
        this.absorbidoPorArmadura = impacto.absorbidoPorArmadura();
        this.danioVida = impacto.danioVida();
        this.vidaRestante = impacto.vidaRestante();
        this.armaduraRestante = impacto.armaduraRestante();
        this.eliminado = impacto.eliminado();
        this.respuestaA2 = respuestaA2;
        this.respuestaA3 = respuestaA3;
    }

    @Override
    public String inspeccionar() {
        return "Jugada #" + id + ": " + atacante + " lanza " + granada + " a " + distancia + " m de " + objetivo
                + " -> " + danioExplosion + " de daño (" + danioVida + " a la vida)" + (eliminado ? " - ELIMINADO" : "");
    }

    public Long getId() { return id; }
    public LocalDateTime getMomento() { return momento; }
    public Long getAtacanteId() { return atacanteId; }
    public String getAtacante() { return atacante; }
    public Long getObjetivoId() { return objetivoId; }
    public String getObjetivo() { return objetivo; }
    public Long getGranadaId() { return granadaId; }
    public String getGranada() { return granada; }
    public double getDistancia() { return distancia; }
    public double getFactorDistanciaA2() { return factorDistanciaA2; }
    public int getDanioExplosion() { return danioExplosion; }
    public int getAbsorbidoPorArmadura() { return absorbidoPorArmadura; }
    public int getDanioVida() { return danioVida; }
    public int getVidaRestante() { return vidaRestante; }
    public int getArmaduraRestante() { return armaduraRestante; }
    public boolean isEliminado() { return eliminado; }
    public String getRespuestaA2() { return respuestaA2; }
    public String getRespuestaA3() { return respuestaA3; }
}
