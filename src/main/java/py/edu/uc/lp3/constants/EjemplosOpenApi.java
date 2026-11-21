package py.edu.uc.lp3.constants;

/** Ejemplos de error que se muestran en el OpenAPI (constantes: las anotaciones las exigen). */
public final class EjemplosOpenApi {

    private EjemplosOpenApi() {
    }

    public static final String VALOR_INVALIDO = """
            {"status":400,"error":"VALOR_INVALIDO","excepcion":"ValorInvalidoException","mensaje":"vida inválido: 150 (0..100)"}""";
    public static final String JSON_INVALIDO = """
            {"status":400,"error":"JSON_INVALIDO","excepcion":"HttpMessageNotReadableException","mensaje":"El cuerpo no se puede leer: revisá el JSON, el campo \\"tipo\\" y el \\"equipo\\""}""";
    public static final String NO_ENCONTRADO = """
            {"status":404,"error":"RECURSO_NO_ENCONTRADO","excepcion":"RecursoNoEncontradoException","mensaje":"No existe el arma con id 99"}""";
    public static final String SIN_MUNICION = """
            {"status":409,"error":"SIN_MUNICION","excepcion":"SinMunicionException","mensaje":"La ráfaga necesita 5 balas y el cargador tiene 3: hay que recargar"}""";
    public static final String GRANADA_USADA = """
            {"status":409,"error":"GRANADA_USADA","excepcion":"GranadaUsadaException","mensaje":"HE Grenade ya fue lanzada: una granada es de un solo uso"}""";
    public static final String JUGADOR_ELIMINADO = """
            {"status":422,"error":"JUGADOR_ELIMINADO","excepcion":"JugadorEliminadoException","mensaje":"El jugador ya está eliminado (vida 0): no puede recibir más daño"}""";
}
