# Refactor conservador - AppMorososNo

Cambios aplicados:

- Ordenados métodos por regiones en las clases principales.
- Eliminados imports/campos sobrantes.
- Extraídos métodos auxiliares pequeños en `ServiciosUsuario` para mejorar lectura.
- Mantenidos nombres de clases, métodos y variables principales.
- Añadido `Evento.importarParticipante()` para que la carga desde fichero reconstruya históricos sin saltarse validaciones básicas.
- `GestorFicheros` usa `importarParticipante()` al cargar datos.
- Revisados participantes, pagos, rankings y usuarios desactivados sin cambiar el flujo de uso.

Comprobaciones:

- El código principal compila con `javac`.
- Los tests intensivos de reglas de negocio pasan: 13/13 OK.

No se ha cambiado:

- Arquitectura general.
- Paquetes principales.
- Nombres principales de métodos y variables.
- Funcionamiento por consola.
- Base de datos o GUI.
