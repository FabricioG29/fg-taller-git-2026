package py.edu.uc.lp3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.uc.lp3.domain.Disparo;

/** Historial de disparos de la simulación. */
public interface DisparoRepository extends JpaRepository<Disparo, Long> {

    /** Todo lo que recibió un jugador, en orden. */
    List<Disparo> findByObjetivoIdOrderByIdAsc(Long objetivoId);

    /** Todo lo que disparó un jugador, en orden. */
    List<Disparo> findByAtacanteIdOrderByIdAsc(Long atacanteId);
}
