# Registro de uso de IA

## Consulta 1

- Fecha: 2026-11-07
- Herramienta: Codex
- Prompt: Iniciar la Fase 2 con IA en la rama existente y preparar el primer commit del laboratorio.
- Qué respondió (resumen): Revisar el estado de la rama de mejora existente y trabajar únicamente en Semana_12; agregar pruebas unitarias para ListaViewModel.
- Qué usamos / qué cambiamos y por qué: Incorporamos en la rama mejora-ia el proyecto Semana_12 que no estaba presente en ella. Agregamos cuatro pruebas unitarias usando un repositorio falso y un notificador falso.
- Qué aprendimos: Un repositorio falso permite probar los estados de lista, error, modo sin conexión y alternancia de favoritos sin depender de Firebase, Room ni la API.

## Consulta 2

- Fecha: 2026-11-07
- Herramienta: Codex
- Prompt: Mejorar los mensajes de error de la lista para distinguir fallos de conexión, errores HTTP y errores inesperados, y comprobarlos con pruebas.
- Qué respondió (resumen): Usar IOException, HttpException y un caso general para mostrar mensajes claros sin filtrar detalles internos.
- Qué usamos / qué cambiamos y por qué: El ListaViewModel ahora traduce esos tres tipos de error a mensajes comprensibles. Añadimos pruebas para los tres casos.
- Qué aprendimos: Clasificar errores por tipo permite orientar a la persona usuaria sin mostrar mensajes técnicos internos.

## Consulta 3

- Fecha: 2026-11-07
- Herramienta: Codex
- Prompt: Mejorar la vista en la Fase 2 y completar la carpeta navigation que está vacía.
- Qué respondió (resumen): El PDF contempla `ui/navigation/AppNav.kt`; la navegación estaba centralizada en MainActivity y podía separarse en un componente de navegación.
- Qué usamos / qué cambiamos y por qué: Creamos AppNav para reunir la navegación por rol, detalle, favoritos y administración. Centralizamos el título y el cierre de sesión en una barra superior y quitamos títulos repetidos en las pantallas.
- Qué aprendimos: Mantener la selección de pantallas en el paquete navigation hace que MainActivity se enfoque en preparar ViewModels y sesión.
