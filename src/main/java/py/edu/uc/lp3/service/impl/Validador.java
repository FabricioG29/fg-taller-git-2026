package py.edu.uc.lp3.service.impl;

import py.edu.uc.lp3exceptions.ValorInvalidoException;

/**
 * Chequeos reutilizables de la capa service. Cada uno lanza ValorInvalidoException
 * (400) con un mensaje que nombra el campo, antes de que se construya o se toque
 * una entidad. Las entidades no validan: confían en que el service ya lo hizo.
 */
final class Validador {

    private Validador() {
    }

    static <T> T requerido(T valor, String campo) {
        if (valor == null) {
            throw new ValorInvalidoException("Falta el campo obligatorio \"" + campo + "\"");
        }
        return valor;
    }

    static String texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank()) {
            throw new ValorInvalidoException("\"" + campo + "\" no puede estar vacío");
        }
        String limpio = valor.trim();
        if (limpio.length() > maximo) {
            throw new ValorInvalidoException("\"" + campo + "\" no puede superar " + maximo + " caracteres");
        }
        return limpio;
    }

    static int entero(Integer valor, String campo, int minimo, int maximo) {
        int v = requerido(valor, campo);
        if (v < minimo || v > maximo) {
            throw new ValorInvalidoException(campo + " inválido: " + v + " (" + minimo + ".." + maximo + ")");
        }
        return v;
    }

    /** Rango cerrado [minimo, maximo]; rechaza NaN e infinito. */
    static float decimal(Float valor, String campo, float minimo, float maximo) {
        float v = requerido(valor, campo);
        if (!Float.isFinite(v) || v < minimo || v > maximo) {
            throw new ValorInvalidoException(campo + " inválido: " + v + " (" + minimo + ".." + maximo + ")");
        }
        return v;
    }

    /** Estrictamente mayor que 0 (y finito). */
    static float positivo(Float valor, String campo) {
        float v = requerido(valor, campo);
        if (!Float.isFinite(v) || v <= 0f) {
            throw new ValorInvalidoException(campo + " debe ser mayor que 0: " + v);
        }
        return v;
    }
}
