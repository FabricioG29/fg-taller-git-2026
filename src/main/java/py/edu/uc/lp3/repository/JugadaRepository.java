package py.edu.uc.lp3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.uc.lp3.domain.Jugada;

/** Jugadas del escenario cruzado que A2 y A3 aceptaron. */
public interface JugadaRepository extends JpaRepository<Jugada, Long> {

    List<Jugada> findAllByOrderByIdAsc();
}
