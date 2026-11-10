package py.edu.uc.lp3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipo;

/**
 * Repositorio de la familia Arma (todas las hijas viven en la tabla "arma").
 * Es una interface: Spring Data genera la implementación a partir del nombre
 * de cada mensaje. Acá no hay lógica del juego.
 */
public interface ArmaRepository extends JpaRepository<Arma, Long> {

    /** Armas que puede comprar un bando (sin las de "AMBOS"). */
    List<Arma> findByEquipoOrderByIdAsc(Equipo equipo);

    /** Armas al alcance del presupuesto de la ronda, de la más barata a la más cara. */
    List<Arma> findByPrecioLessThanEqualOrderByPrecioAsc(int precioMaximo);
}
