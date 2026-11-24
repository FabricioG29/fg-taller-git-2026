package py.edu.uc.lp3.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Lo que leemos de la respuesta de A2 (esquema ResultadoSimulacion de su OpenAPI).
 * Solo los campos que usa el escenario; el resto se ignora.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExplosionA2(Boolean enRadioAlcance, Double distanciaMetros, Double radioExplosion,
                          Double danioNominal, Double danioAtenuadoDistancia, String detalleRegla) {

    /** Fracción del daño que llega a esa distancia: 1 en el centro, 0 fuera del radio. */
    public double factorDeDistancia() {
        if (Boolean.FALSE.equals(enRadioAlcance)) {
            return 0;
        }
        double factor = danioAtenuadoDistancia / danioNominal;
        return Math.max(0, Math.min(1, factor));
    }

    public boolean completa() {
        return danioNominal != null && danioNominal > 0 && danioAtenuadoDistancia != null;
    }
}
