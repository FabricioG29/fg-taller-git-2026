# Bitácora de asistencia de IA

| Dato | Valor |
|---|---|
| Asistente | Claude (app de escritorio de Claude, modo Cowork), de Anthropic |
| Modelo | `claude-opus-5-5` (en el selector de la app figura como "Opus 5.5") |
| Período | 23 de septiembre al 10 de noviembre de 2026 (POO-06 y POO-08) |
| Alumno | Fabricio González (`FabricioG29`) |

## Resumen de los prompts — POO-06 (taller de Git y revisión)

1. **Dónde trabajar.** Le pasé la guía del taller (TALLER_GIT.md) y el enunciado, y le pregunté si tenía que hacer todo en la VM, donde tengo OpenCode, o en el Mac.
2. **Diagrama.** Le pasé el link de mermaid.live con mi diagrama de CS2 de POO-03 y le pedí que me dijera qué le faltaba para cumplir la opción B.
3. **Proyecto completo.** Como no tenía el código de POO-03, le pedí que generara todo lo que pide la consigna: las clases del modelo a partir de mi diagrama, el método abstracto con dos hijas, los controllers, el README con Mermaid y una bitácora de entrega.
4. **Recargar sin `if`.** Al releer la opción B le pedí revisar que el inventario pudiera disparar, recargar y mostrarse en la tienda con cualquier arma; agregó `recargar()` al contrato de `Arma`.
5. **Git y GitHub.** Le pregunté por errores al clonar (usuario equivocado y repo con otro nombre), cómo crear el token para el push y por qué el push decía "Everything up-to-date" (me faltaba el commit).
6. **VM por SSH.** Me pidieron trabajar en la VM conectado desde el Mac; le pedí los pasos para VirtualBox con NAT, reenvío de puertos y túnel para ver el puerto 8080 desde el navegador del Mac.
7. **Spring Boot.** Le pregunté cómo generar el proyecto desde la terminal con las mismas dependencias que el de clase (las saqué del `pom.xml` que generé en clase).
8. **Tildes.** Le mostré que el JSON salía con `GonzÃ¡lez` y me dio la configuración UTF-8 y el ajuste en `ManejoErrores`.
9. **Rúbrica.** Le pasé la rúbrica y le pregunté si el proyecto cumplía. Encontró que con `precision=NaN` en la URL se podía crear una pistola inválida y que no había sobrecarga; pedí el arreglo.
10. **Template.** Le pedí reorganizar las clases según `alefq/lp3-template-tp` (`domain`, `rest/controller`, `service`, `constants`).
11. **Entrega final.** Con los requisitos nuevos de corrección le pedí la sobrecarga de un mensaje (`disparar(int veces)`), el apartado de sobrecarga y sobreescritura del README y esta bitácora.

## Resumen de los prompts — POO-08 (CRUD, H2 e Insomnia)

1. **Enunciado.** Le pasé la consigna de POO-08 (persistencia en H2, CRUD, dos reglas, excepciones, interface, Insomnia) y le pedí que la aplicara a mi modelo de CS2 sin romper lo de POO-06 (método abstracto `usar()`, sobrecarga y sobreescritura).
2. **Diseño de la persistencia.** Le pregunté qué tres clases persistir y qué estrategia de herencia de JPA usar. Quedó la familia `Arma` en una sola tabla (`SINGLE_TABLE`), `Jugador` y `Disparo`.
3. **Ocultamiento con JPA.** Le pregunté cómo convive el constructor vacío que pide JPA con no tener setters; se usó un constructor `protected` y los cambios por mensajes (`renombrar`, `cambiarPrecio`).
4. **Reglas del juego.** Le pedí dos reglas de CS2 que cambien más de un objeto a la vez: munición (el cargador vacío no dispara) y daño con chaleco (absorbe la mitad, vida 0 elimina).
5. **Excepciones.** Le pedí una jerarquía propia con un handler por tipo y que justificara excepción comprobada vs. no comprobada.
6. **JSON polimórfico.** Le pregunté cómo hacer que el JSON diga qué tipo de arma se crea y se lista; se usó `@JsonTypeInfo` con el campo `"tipo"`.
7. **Insomnia y README.** Le pedí la colección con el orden de prueba y un pedido por cada error, el diagrama Mermaid actualizado y las respuestas a "Qué hay que pensar".
8. **Commits.** Le pedí separar el trabajo en commits por parte (propiedades, excepciones, entidades, repositorios, CRUD, simulación, Insomnia, README).

## Resumen de los prompts — POO-09 (APIs cruzadas, OpenAPI y corrección de POO-08)

1. **Corrección de POO-08.** Le pasé la nota de POO-08 (criterio 2 con 1 y criterio 3 con 0) y le pedí cerrar cada observación: services para que ningún controller use un repositorio; validaciones, reglas y excepciones fuera del dominio (quedaron en `ArmaService`, `JugadorService` y `ReglasDeJuegoService`); y la granada como consumible de un solo uso, sin cooldown, con su propia excepción 409 controlada en el service.
2. **Revisión punto por punto.** Le volví a pasar el texto de la corrección y le pregunté si cada ítem estaba cerrado; me mostró dónde quedó cada cosa en el código.
3. **OpenAPI.** Le pregunté cómo publicar OpenAPI 3 en Spring Boot 4; usamos springdoc (línea 3.x) con ejemplos de cada error, y publiqué mis dos reglas sin estado (`/api/reglas/municion` y `/api/reglas/danio`) para que las consulten mis compañeros.
4. **Mensaje al grupo.** Le pedí qué tenía que mandarles a mis compañeros (repo, OpenAPI, puertos y mis reglas con ejemplos).
5. **Clientes y escenario.** Le pasé lo que me mandaron Diana Aquino y Matias Enciso (sus URLs, rutas y reglas) y le pedí los clientes hacia A2 y A3 leyendo solo su README y su OpenAPI, sin copiar su código, y un escenario de CS2 que necesite las dos validaciones: «lanzar una granada a un rival».
6. **Orden y errores.** Le pregunté en qué orden consultar y cómo atribuir el rechazo; quedó A2 (Matias, daño por distancia) antes que A3 (Diana, fuego amigo y alcance), con 409 `RECHAZO_A2`/`RECHAZO_A3` y 503 si alguno no responde.
7. **Insomnia, README y commits.** Le pedí la colección con las tres bases, el recorrido con curl que guarda las respuestas de ejemplo, el README con el mapa del grupo y las respuestas de «Qué hay que pensar», y commits separados por parte.

## Qué hice yo

- Ejecuté cada paso en la VM (Ubuntu) desde la terminal del Mac por SSH: clonado, generación del proyecto, compilación, commits y push.
- Probé todos los endpoints en el navegador, incluidos los casos de error (`danio=-5`, `precision=NaN`), y mandé capturas para corregir lo que fallaba.
- Revisé que el diagrama Mermaid se dibujara en GitHub y que coincidiera con el código.
- Decidí el dominio (CS2) y el diagrama de partida, y elegí qué ajustes del diagrama aceptar.
- En POO-08 apliqué los commits en la VM, arranqué el servicio, recorrí la colección de Insomnia contra mi servicio y revisé las tablas en la consola de H2.
- En POO-09 apliqué los commits en la VM, corrí `./mvnw test`, probé el OpenAPI y las reglas con `curl`, coordiné con Diana y Matias los puertos y las reglas que nos consultamos, levanté los tres servicios y corrí el recorrido del escenario (feliz, rechazo de A2 y rechazo de A3).
