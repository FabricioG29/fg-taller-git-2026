package py.edu.uc.lp3.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipo;
import py.edu.uc.lp3.domain.Escopeta;
import py.edu.uc.lp3.domain.Francotirador;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.domain.Inventario;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.domain.RifleAsalto;
import py.edu.uc.lp3.domain.Subfusil;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.service.ArmaService;
import py.edu.uc.lp3.service.dto.ActualizarArmaRequest;
import py.edu.uc.lp3.service.dto.Comportamientos;
import py.edu.uc.lp3.service.dto.CrearArmaRequest;
import py.edu.uc.lp3.service.dto.CrearArmaRequest.EscopetaRequest;
import py.edu.uc.lp3.service.dto.CrearArmaRequest.FrancotiradorRequest;
import py.edu.uc.lp3.service.dto.CrearArmaRequest.GranadaRequest;
import py.edu.uc.lp3.service.dto.CrearArmaRequest.PistolaRequest;
import py.edu.uc.lp3.service.dto.CrearArmaRequest.RifleRequest;
import py.edu.uc.lp3.service.dto.CrearArmaRequest.SubfusilRequest;
import py.edu.uc.lp3.service.dto.PruebaPistola;
import py.edu.uc.lp3exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * CRUD de armas. Es la única capa que habla con el ArmaRepository y la que valida:
 * cada campo se revisa acá antes de llamar al constructor de la entidad, así un
 * JSON ilegal responde 400 y la base no cambia.
 */
@Service
public class ArmaServiceImpl implements ArmaService {

    public static final int PRECIO_MAXIMO = 10_000;
    public static final int DANIO_MAXIMO = 500;
    public static final int LARGO_NOMBRE = 60;
    public static final float PRECISION_PISTOLA_DE_FABRICA = 0.9f;

    private final ArmaRepository armaRepository;

    public ArmaServiceImpl(ArmaRepository armaRepository) {
        this.armaRepository = armaRepository;
    }

    @Override
    @Transactional
    public Arma crear(CrearArmaRequest pedido) {
        Validador.requerido(pedido, "cuerpo");
        Arma arma = switch (pedido) { // el "tipo" del JSON ya eligió el record; acá se valida cada hija
            case PistolaRequest p -> crearPistola(p);
            case RifleRequest r -> new RifleAsalto(nombre(r.nombre()), precio(r.precio()), equipo(r.equipo()),
                    danioDeFuego(r.danio()), precision(r.precision()), cargador(r.cargador()),
                    reserva(r.reserva()), Validador.positivo(r.cadencia(), "cadencia"));
            case FrancotiradorRequest f -> new Francotirador(nombre(f.nombre()), precio(f.precio()),
                    equipo(f.equipo()), danioDeFuego(f.danio()), precision(f.precision()), cargador(f.cargador()),
                    reserva(f.reserva()), Validador.decimal(f.zoom(), "zoom", 1f, 50f),
                    Validador.decimal(f.penetracion(), "penetracion", 0f, 1f));
            case EscopetaRequest e -> new Escopeta(nombre(e.nombre()), precio(e.precio()), equipo(e.equipo()),
                    danioDeFuego(e.danio()), precision(e.precision()), cargador(e.cargador()), reserva(e.reserva()),
                    Validador.entero(e.perdigones(), "perdigones", 1, 20),
                    Validador.positivo(e.distanciaEfectiva(), "distanciaEfectiva"));
            case SubfusilRequest s -> new Subfusil(nombre(s.nombre()), precio(s.precio()), equipo(s.equipo()),
                    danioDeFuego(s.danio()), precision(s.precision()), cargador(s.cargador()), reserva(s.reserva()),
                    Validador.decimal(s.controlRetroceso(), "controlRetroceso", 0f, 1f));
            case GranadaRequest g -> new Granada(nombre(g.nombre()), precio(g.precio()), equipo(g.equipo()),
                    Validador.entero(g.danio(), "danio", 0, DANIO_MAXIMO),
                    Validador.positivo(g.radioExplosion(), "radioExplosion"),
                    Validador.decimal(g.tiempoActivacion(), "tiempoActivacion", 0f, 60f),
                    Validador.texto(g.efecto(), "efecto", 100),
                    cooldown(g.cooldownMs()));
        };
        return armaRepository.save(arma);
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
            arma.renombrar(nombre(pedido.nombre()));
        }
        if (pedido.precio() != null) {
            arma.cambiarPrecio(precio(pedido.precio()));
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
        Pistola pistola = crearPistola(new PistolaRequest(nombre, precio, equipo, danio, precision, cargador, reserva));
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

    /** Sin precision/cargador/reserva usa el constructor simple (sobrecarga); con los tres, el completo. */
    private Pistola crearPistola(PistolaRequest p) {
        if (p.precision() == null && p.cargador() == null && p.reserva() == null) {
            return new Pistola(nombre(p.nombre()), precio(p.precio()), equipo(p.equipo()), danioDeFuego(p.danio()));
        }
        return new Pistola(nombre(p.nombre()), precio(p.precio()), equipo(p.equipo()), danioDeFuego(p.danio()),
                precision(p.precision()), cargador(p.cargador()), reserva(p.reserva()));
    }

    // --- Invariantes de la familia Arma (antes estaban en los constructores) ---

    private static String nombre(String nombre) {
        return Validador.texto(nombre, "nombre", LARGO_NOMBRE);
    }

    private static int precio(Integer precio) {
        return Validador.entero(precio, "precio", 0, PRECIO_MAXIMO);
    }

    private static Equipo equipo(Equipo equipo) {
        return Validador.requerido(equipo, "equipo");
    }

    /** Un arma de fuego siempre hace daño (> 0). */
    private static int danioDeFuego(Integer danio) {
        return Validador.entero(danio, "danio", 1, DANIO_MAXIMO);
    }

    private static float precision(Float precision) {
        float p = Validador.decimal(precision, "precision", 0f, 1f);
        if (p == 0f) {
            throw new ValorInvalidoException("precision debe ser mayor que 0");
        }
        return p;
    }

    private static int cargador(Integer cargador) {
        return Validador.entero(cargador, "cargador", 1, 200);
    }

    private static int reserva(Integer reserva) {
        return Validador.entero(reserva, "reserva", 0, 1_000);
    }

    private static long cooldown(Long cooldownMs) {
        long c = Validador.requerido(cooldownMs, "cooldownMs");
        if (c < 0) {
            throw new ValorInvalidoException("cooldownMs no puede ser negativo: " + c);
        }
        return c;
    }

    private Inventario inventarioDeEjemplo() {
        Inventario inventario = new Inventario();
        inventario.agregar(new Pistola("Glock-18", 200, Equipo.TERRORISTAS, 30)); // constructor sobrecargado
        inventario.agregar(new Granada("HE Grenade", 300, Equipo.AMBOS, 98, 5.0f, 1.5f,
                "Explosión de fragmentación", 3000));
        return inventario;
    }
}
