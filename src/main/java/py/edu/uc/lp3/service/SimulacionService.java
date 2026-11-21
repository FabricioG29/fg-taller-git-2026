package py.edu.uc.lp3.service;

import java.util.List;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Disparo;
import py.edu.uc.lp3.domain.ReporteSimulacion;

/** Casos de uso de la simulación. Coordina; las reglas viven en el dominio. */
public interface SimulacionService {

    /** El atacante dispara "veces" al objetivo con el arma (reglas de munición y de daño). */
    ReporteSimulacion simularDisparos(Long atacanteId, Long objetivoId, Long armaId, int veces);

    /** Pasa balas de la reserva al cargador (regla de munición) y lo persiste. */
    Arma recargar(Long armaId);

    /** Historial persistido; filtra por objetivo o por atacante si llegan. */
    List<Disparo> historial(Long objetivoId, Long atacanteId);
}
