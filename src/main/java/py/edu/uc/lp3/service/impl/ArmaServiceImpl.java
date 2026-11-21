package py.edu.uc.lp3.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.domain.Inventario;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.service.ArmaService;
import py.edu.uc.lp3.service.dto.ActualizarArmaRequest;
import py.edu.uc.lp3.service.dto.Comportamientos;
import py.edu.uc.lp3.service.dto.CrearArmaRequest;
import py.edu.uc.lp3.service.dto.PruebaPistola;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** CRUD de armas: el controller llega acá y solo acá se habla con el ArmaRepository. */
@Service
public class ArmaServiceImpl implements ArmaService {

    private final ArmaRepository armaRepository;

    public ArmaServiceImpl(ArmaRepository armaRepository) {
        this.armaRepository = armaRepository;
    }

    @Override
    @Transactional
    public Arma crear(CrearArmaRequest pedido) {
        return armaRepository.save(pedido.crear());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Arma> listar(Equipo equipo, Integer precioMaximo) {
        if (equipo != null) {
            return armaRepository.findByEquipoOrderByIdAsc(equipo);
        }
        if (precioMaximo != null) {
            return armaRepository.findByPrecioLessThanEqualOrderByPrecioAsc(precioMaximo);
        }
        return armaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Arma buscar(Long id) {
        return armaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Arma", id));
    }

    @Override
    @Transactional
    public Arma actualizar(Long id, ActualizarArmaRequest pedido) {
        if (pedido.nombre() == null && pedido.precio() == null) {
            throw new ValorInvalidoException("Nada para actualizar: enviá \"nombre\" y/o \"precio\"");
        }
        Arma arma = buscar(id);
        if (pedido.nombre() != null) {
            arma.renombrar(pedido.nombre());
        }
        if (pedido.precio() != null) {
            arma.cambiarPrecio(pedido.precio());
        }
        return armaRepository.save(arma);
    }

    @Override
    @Transactional
    public void borrar(Long id) {
        armaRepository.delete(buscar(id));
    }

    @Override
    public PruebaPistola probarPistola(String nombre, int precio, Equipo equipo, int danio,
                                       float precision, int cargador, int reserva) {
        Pistola pistola = new Pistola(nombre, precio, equipo, danio, precision, cargador, reserva);
        String municion = pistola.getMunicionCargador() + "/" + pistola.getMunicionReserva();
        return new PruebaPistola(pistola.mostrarEnTienda(), municion, pistola.usar());
    }

    @Override
    public Comportamientos comportamientosDeEjemplo() {
        Inventario inventario = inventarioDeEjemplo();
        return new Comportamientos(inventario.mostrarTienda(), inventario.usarTodas(), inventario.recargarTodas());
    }

    @Override
    public List<String> tiendaDeEjemplo() {
        return inventarioDeEjemplo().mostrarTienda();
    }

    private Inventario inventarioDeEjemplo() {
        Inventario inventario = new Inventario();
        inventario.agregar(new Pistola("Glock-18", 200, Equipo.TERRORISTAS, 30)); // constructor sobrecargado
        inventario.agregar(new Granada("HE Grenade", 300, Equipo.AMBOS, 98, 5.0f, 1.5f,
                "Explosión de fragmentación", 3000));
        return inventario;
    }
}
