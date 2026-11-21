package py.edu.uc.lp3.service.impl;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.EstadoUso;
import py.edu.uc.lp3.domain.Impacto;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.service.ReglasDeJuegoService;
import py.edu.uc.lp3.service.dto.EvaluacionMunicion;
import py.edu.uc.lp3.service.dto.ReglaDanioRequest;
import py.edu.uc.lp3.service.dto.ReglaMunicionRequest;
import py.edu.uc.lp3exceptions.GranadaUsadaException;
import py.edu.uc.lp3exceptions.JugadorEliminadoException;
import py.edu.uc.lp3exceptions.SinMunicionException;
import py.edu.uc.lp3exceptions.ValorInvalidoException;

/** Implementación de las dos reglas. No tiene estado ni toca la base. */
@Service
public class ReglasDeJuegoServiceImpl implements ReglasDeJuegoService {

    public static final int VIDA_MAXIMA = Jugador.VIDA_MAXIMA;
    public static final int ARMADURA_MAXIMA = Jugador.ARMADURA_MAXIMA;
    public static final int DANIO_MAXIMO = 500;
    public static final int CARGADOR_MAXIMO = 200;
    public static final int BALAS_POR_DISPARO_MAXIMO = 10;
    public static final int DISPAROS_MAXIMO = 30;

    @Override
    public void verificarQuePuedeUsarse(Arma arma) {
        EstadoUso estado = arma.estadoDeUso(); // cada hija informa su estado (sobreescritura)
        if (estado == EstadoUso.SIN_MUNICION) {
            throw new SinMunicionException(arma.getNombre() + ": cargador vacío, hay que recargar");
        }
        if (estado == EstadoUso.USADA) {
            throw new GranadaUsadaException(arma.getNombre() + " ya fue lanzada: una granada es de un solo uso");
        }
    }

    @Override
    public void verificarQuePuedeDisparar(Jugador atacante, Arma arma) {
        if (atacante.estaEliminado()) {
            throw new JugadorEliminadoException(atacante.getNombre() + " está eliminado y no puede disparar");
        }
        if (!arma.esDelEquipo(atacante.getEquipo())) {
            throw new ValorInvalidoException(arma.getNombre() + " es de " + arma.obtenerEquipo() + ": "
                    + atacante.getNombre() + " (" + atacante.getEquipo() + ") no la puede usar");
        }
    }

    @Override
    public void verificarQuePuedeRecibirDanio(Jugador objetivo) {
        if (objetivo.estaEliminado()) {
            throw new JugadorEliminadoException(objetivo.getNombre() + " ya está eliminado: no puede recibir más daño");
        }
    }

    @Override
    public Impacto calcularImpacto(int vida, int armadura, int danio) {
        if (vida < 0 || vida > VIDA_MAXIMA) {
            throw new ValorInvalidoException("Vida inválida: " + vida + " (0.." + VIDA_MAXIMA + ")");
        }
        if (armadura < 0 || armadura > ARMADURA_MAXIMA) {
            throw new ValorInvalidoException("Armadura inválida: " + armadura + " (0.." + ARMADURA_MAXIMA + ")");
        }
        if (danio < 0 || danio > DANIO_MAXIMO) {
            throw new ValorInvalidoException("Daño inválido: " + danio + " (0.." + DANIO_MAXIMO + ")");
        }
        if (vida == 0) {
            throw new JugadorEliminadoException("El jugador ya está eliminado (vida 0): no puede recibir más daño");
        }
        int absorbido = Math.min(armadura, danio / 2);
        int aLaVida = Math.min(vida, danio - absorbido);
        int vidaRestante = vida - aLaVida;
        return new Impacto(danio, absorbido, aLaVida, vidaRestante, armadura - absorbido, vidaRestante == 0);
    }

    @Override
    public EvaluacionMunicion evaluarMunicion(ReglaMunicionRequest pedido) {
        Validador.requerido(pedido, "cuerpo");
        int cargador = Validador.entero(pedido.cargador(), "cargador", 0, CARGADOR_MAXIMO);
        int porDisparo = pedido.balasPorDisparo() == null ? 1
                : Validador.entero(pedido.balasPorDisparo(), "balasPorDisparo", 1, BALAS_POR_DISPARO_MAXIMO);
        int disparos = Validador.entero(pedido.disparos(), "disparos", 1, DISPAROS_MAXIMO);
        int necesarias = porDisparo * disparos;
        if (cargador < necesarias) {
            throw new SinMunicionException("La ráfaga necesita " + necesarias + " balas y el cargador tiene "
                    + cargador + ": hay que recargar");
        }
        return new EvaluacionMunicion(true, cargador, necesarias, cargador - necesarias);
    }

    @Override
    public Impacto evaluarDanio(ReglaDanioRequest pedido) {
        Validador.requerido(pedido, "cuerpo");
        return calcularImpacto(Validador.requerido(pedido.vida(), "vida"),
                Validador.requerido(pedido.armadura(), "armadura"),
                Validador.requerido(pedido.danio(), "danio"));
    }
}
