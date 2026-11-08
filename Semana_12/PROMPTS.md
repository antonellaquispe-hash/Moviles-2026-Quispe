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

## Consulta 4

- Fecha: 2026-11-07
- Herramienta: Codex
- Prompt: Mejorar la vista con más color y aclarar si el PDF solicita pantallas distintas para administrador y usuario.
- Qué respondió (resumen): Propuso una paleta cálida, tarjetas con imágenes reales de álbumes y una pantalla de acceso más cuidada; el PDF solicita acceso compartido y funciones diferenciadas por rol.
- Qué usamos / qué cambiamos y por qué: Aplicamos una paleta ámbar, ciruela y coral, refinamos login, tarjetas, detalle y panel, y conectamos las carátulas reales con Coil. Se eligió Coil 2.7.0 para mantener compatibilidad con Kotlin 2.2.10.
- Qué aprendimos: La interfaz puede compartir catálogo y favoritos mientras muestra el acceso al panel solo a la cuenta administradora.

## Consulta 5

- Fecha: 2026-11-07
- Herramienta: Codex
- Prompt: Corregir el tema porque el trabajo es individual y corresponde a Publicaciones; revisar la configuración Firebase mostrada en capturas.
- Qué respondió (resumen): La API actual de iTunes no correspondía a Publicaciones. Firestore usa los campos `Administrador` y `Usuario`, mientras la app solo leía `rol`.
- Qué usamos / qué cambiamos y por qué: Cambiamos Retrofit a JSONPlaceholder `GET /posts` y `GET /posts/{id}`, adaptamos DTO, mapeo, repositorio, textos e interfaz. La lectura del rol ahora reconoce `rol` y los campos existentes `Administrador` o `Usuario`. Creamos una base Room nueva para evitar que una actualización mezcle publicaciones con álbumes guardados anteriormente.
- Qué aprendimos: El tema asignado define los endpoints de lista y detalle; el documento de Firestore debe leerse de acuerdo con los campos que realmente existen.
