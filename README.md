# fg-taller-git-2026

Taller de Git y modelado orientado a objetos — Lenguaje de Programación 3 (CYT646), 2026.
Servicio HTTP (Spring Boot, API REST) con el modelado de **Counter-Strike 2**.

Arrancar: `./mvnw spring-boot:run` → http://localhost:8080/

## Diagrama de clases (dominio CS2)

```mermaid
classDiagram
  direction TB

  class Arma {
    <<abstract>>
    -String nombre
    -int precio
    -Equipo equipo
    -int danio
    +usar() ResultadoAccion*
    +recargar() ResultadoAccion
    +mostrarEnTienda() String
    +obtenerPrecio() int
    +obtenerEquipo() Equipo
    #getDanioBase() int
  }

  class ArmaDeFuego {
    <<abstract>>
    -float precision
    -int capacidadCargador
    -int municionCargador
    -int municionReserva
    -float tiempoRecarga
    +usar() ResultadoAccion
    +disparar() ResultadoAccion
    +recargar() ResultadoAccion
    #calcularDanio() int
    #balasPorDisparo() int
  }

  class Granada {
    -float radioExplosion
    -float tiempoActivacion
    -String efecto
    -long cooldownMs
    -long ultimoLanzamientoMs
    +usar() ResultadoAccion
    +lanzar() ResultadoAccion
    +recargar() ResultadoAccion
    -detonar() ResultadoAccion
    -esperaRestanteMs() long
    +esLetal() boolean
  }

  class Pistola {
    -boolean modoRafaga
    +activarModoRafaga() void
    #balasPorDisparo() int
    #calcularDanio() int
  }

  class RifleAsalto {
    -ModoDisparo modoDisparo
    -float cadenciaDisparo
    +cambiarModo(ModoDisparo m) void
    #balasPorDisparo() int
  }

  class Francotirador {
    -float zoom
    -float penetracion
    -boolean zoomActivo
    +activarZoom() void
    +desactivarZoom() void
    #calcularDanio() int
  }

  class Escopeta {
    -int numeroPerdigones
    -float distanciaEfectiva
    #calcularDanio() int
  }

  class Subfusil {
    -float controlRetroceso
    #balasPorDisparo() int
    #calcularDanio() int
  }

  class Inventario {
    -List~Arma~ armas
    +agregar(Arma a) void
    +usarTodas() List~ResultadoAccion~
    +recargarTodas() List~ResultadoAccion~
    +mostrarTienda() List~String~
  }

  class ResultadoAccion {
    <<record>>
    arma String
    tipo String
    accion String
    danio int
    detalle String
  }

  class Equipo {
    <<enumeration>>
    TERRORISTAS
    ANTITERRORISTAS
    AMBOS
  }

  class ModoDisparo {
    <<enumeration>>
    SEMI
    RAFAGA
    AUTOMATICO
  }

  Arma <|-- ArmaDeFuego
  Arma <|-- Granada
  ArmaDeFuego <|-- Pistola
  ArmaDeFuego <|-- RifleAsalto
  ArmaDeFuego <|-- Francotirador
  ArmaDeFuego <|-- Escopeta
  ArmaDeFuego <|-- Subfusil

  Arma --> Equipo
  RifleAsalto --> ModoDisparo
  Inventario o-- "0..5" Arma
  Arma ..> ResultadoAccion : usar()
```
