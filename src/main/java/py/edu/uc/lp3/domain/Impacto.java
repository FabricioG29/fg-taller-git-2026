package py.edu.uc.lp3.domain;

/** Resultado de aplicar la regla de daño a un jugador (inmutable). */
public record Impacto(int danioRecibido, int absorbidoPorArmadura, int danioVida,
                      int vidaRestante, int armaduraRestante, boolean eliminado) {
}
