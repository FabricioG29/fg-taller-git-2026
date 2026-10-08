# Bitácora de asistencia de IA

| Dato | Valor |
|---|---|
| Asistente | Claude (app de escritorio de Claude, modo Cowork), de Anthropic |
| Modelo | `claude-opus-5-5` (en el selector de la app figura como "Opus 5.5") |
| Período | 23 de septiembre al 8 de octubre de 2026 |
| Alumno | Fabricio González (`FabricioG29`) |

## Resumen de los prompts

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

## Qué hice yo

- Ejecuté cada paso en la VM (Ubuntu) desde la terminal del Mac por SSH: clonado, generación del proyecto, compilación, commits y push.
- Probé todos los endpoints en el navegador, incluidos los casos de error (`danio=-5`, `precision=NaN`), y mandé capturas para corregir lo que fallaba.
- Revisé que el diagrama Mermaid se dibujara en GitHub y que coincidiera con el código.
- Decidí el dominio (CS2) y el diagrama de partida, y elegí qué ajustes del diagrama aceptar.
