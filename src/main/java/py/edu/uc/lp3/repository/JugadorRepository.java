package py.edu.uc.lp3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.Jugador;

/** Repositorio de jugadores. Las consultas extra salen del vocabulario de la ronda. */
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    /** Jugadores de un bando. */
    List<Jugador> findByEquipoOrderByIdAsc(Equipo equipo);

    /** Jugadores que siguen vivos (vida mayor a 0). */
    List<Jugador> findByVidaGreaterThanOrderByIdAsc(int vida);
}
