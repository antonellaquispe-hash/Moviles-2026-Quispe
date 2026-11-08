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

## Consulta 6

- Fecha: 2026-11-07
- Herramienta: Codex
- Prompt: Reemplazar los textos de prueba por publicaciones y contenido real, con fotos reales cuando existan.
- Qué respondió (resumen): JSONPlaceholder sirve datos ficticios de demostración; Forem/DEV ofrece artículos públicos reales con autor, descripción, portada y contenido.
- Qué usamos / qué cambiamos y por qué: Cambiamos la fuente a la API pública de DEV Community, usando `/articles?per_page=30&top=30` para mostrar artículos populares del último mes y `/articles/{id}` para su contenido. Adaptamos los DTO y mostramos portadas del artículo o imagen de perfil del autor cuando la publicación no tiene portada.
- Qué aprendimos: La API de artículos devuelve datos reales escritos por autores y puede incluir una imagen de portada y el cuerpo del artículo.

## Consulta 7

- Fecha: 2026-11-07
- Herramienta: Codex
- Prompt: Traducir la interfaz a español y agregar un modo claro blanco con morado y un modo oscuro negro con morado.
- Qué respondió (resumen): DEV permite filtrar por etiqueta; se puede mostrar contenido marcado como español y alternar el tema visual desde la aplicación.
- Qué usamos / qué cambiamos y por qué: Filtramos por `tag=spanish` y por `language=es`, eliminamos del catálogo local las publicaciones de otro idioma y agregamos un selector persistente de modo claro u oscuro. El modo claro queda como opción inicial.
- Qué aprendimos: La API de Forem incluye el idioma del artículo y admite filtros de etiqueta en el catálogo público.

## Consulta 8

- Fecha: 2026-11-07
- Herramienta: Codex
- Prompt: Ajustar la variante de la API para que coincida con el recurso asignado en el PDF y conservar el trabajo en la rama mejora-ia.
- Qué respondió (resumen): El PDF asigna a Publicaciones uno de los recursos de JSONPlaceholder: `posts`, `albums` o `todos`.
- Qué usamos / qué cambiamos y por qué: Cambiamos DEV Community por `GET /posts` y `GET /posts/{id}`, migramos la caché para retirar los artículos anteriores y mantuvimos Room, favoritos, navegación, idioma de la interfaz y temas visuales.
- Qué aprendimos: La variante del grupo determina qué recurso y endpoints deben usarse, aunque la interfaz pueda tener mejoras propias.
