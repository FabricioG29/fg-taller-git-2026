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
            {"status":404,"error":"RECURSO_NO_ENCONTRADO","excepcion":"RecursoNoEncontradoException","mensaje":"Arma con id 99 no existe"}""";
    public static final String SIN_MUNICION = """
            {"status":409,"error":"SIN_MUNICION","excepcion":"SinMunicionException","mensaje":"La ráfaga necesita 5 balas y el cargador tiene 3: hay que recargar"}""";
    public static final String GRANADA_USADA = """
            {"status":409,"error":"GRANADA_USADA","excepcion":"GranadaUsadaException","mensaje":"HE Grenade ya fue lanzada: una granada es de un solo uso"}""";
    public static final String JUGADOR_ELIMINADO = """
            {"status":422,"error":"JUGADOR_ELIMINADO","excepcion":"JugadorEliminadoException","mensaje":"El jugador ya está eliminado (vida 0): no puede recibir más daño"}""";
    public static final String RECHAZO_A2 = """
            {"status":409,"error":"RECHAZO_A2","excepcion":"RechazoDeCompaneroException","mensaje":"A2 (Matias Enciso) rechazó la jugada con HTTP 400: no se guardó nada","rol":"A2","companero":"Matias Enciso","urlConsultada":"http://localhost:8081/api/simulacion/explosion?distancia=-3.0&conBlindaje=true&fuegoAmigo=false","statusCompanero":400,"respuestaCompanero":"{...}","estadoH2":"SIN_CAMBIOS"}""";
    public static final String RECHAZO_A3 = """
            {"status":409,"error":"RECHAZO_A3","excepcion":"RechazoDeCompaneroException","mensaje":"A3 (Diana Aquino) rechazó la jugada con HTTP 422: no se guardó nada","rol":"A3","companero":"Diana Aquino","urlConsultada":"http://localhost:8082/api/armas/granadas/simular?equipoAtacante=Terrorista&equipoObjetivo=Terrorista&distancia=2.0","statusCompanero":422,"respuestaCompanero":"{...}","estadoH2":"SIN_CAMBIOS"}""";
    public static final String NO_DISPONIBLE = """
            {"status":503,"error":"COMPANERO_NO_DISPONIBLE_A3","excepcion":"CompaneroNoDisponibleException","mensaje":"A3 (Diana Aquino) no está disponible: no responde en http://localhost:8082 (¿está levantado?). No se guardó nada","rol":"A3","companero":"Diana Aquino","urlConsultada":"http://localhost:8082/api/armas/granadas/simular?...","statusCompanero":null,"respuestaCompanero":null,"estadoH2":"SIN_CAMBIOS"}""";
}
