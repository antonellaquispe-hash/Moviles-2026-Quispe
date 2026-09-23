# REQUERIMIENTOS FUNCIONALES

- Autenticación y acceso al Portal Académico
- Visualización del Directorio de Alumnos
- Consulta del Expediente Académico
- Gestión y visualización del Perfil Académico

# PROMT
Actúa como un desarrollador senior de Android especializado en Kotlin y Jetpack Compose.

Mejora únicamente la interfaz visual de mi proyecto actual. Mantén exactamente la navegación y funcionalidad que ya existe y modifica solo el diseño para que las pantallas se vean como las referencias visuales del laboratorio.

Usa un estilo académico moderno, limpio y elegante, con predominio de morado, lila muy claro, blanco y pequeños detalles rosados. Utiliza fondos claros, degradados morados, tarjetas con esquinas redondeadas, sombras suaves, botones redondeados, iconos Material y buena separación entre elementos.

CERRAR SESIÓN
El elemento "Cerrar Sesión Segura" de la pantalla de bienvenida debe funcionar correctamente. Al pulsarlo debe navegar a la pantalla "Portal Académico" mostrada en el PDF del laboratorio, que contiene el título "Portal Académico", el texto "Accede a tu cuenta", los campos "Correo Institucional" y "Contraseña" y el botón "INICIAR SESIÓN".
Mantén la navegación funcionando correctamente y evita crear pantallas duplicadas.

HOME:
Diseña una pantalla de bienvenida con fondo degradado desde morado en la parte superior hacia lila muy claro en la parte inferior. Coloca al centro superior el texto:
"Bienvenido,
Antonella Quispe"
Debajo:
"¿Qué deseas gestionar hoy?"
Mantén los dos accesos actuales como dos tarjetas grandes blancas con bordes redondeados y sombra suave. La primera debe representar "Directorio de Alumnos" con icono de grupo y descripción "Ver y gestionar estudiantes". La segunda debe representar "Mi Perfil Académico" con icono de usuario y descripción "Datos personales y progreso". En la parte inferior coloca "Cerrar Sesión Segura" con icono de salida y tono rojizo suave.

LIST:
Diseña esta pantalla como un "Directorio de Alumnos". Usa una barra superior blanca con flecha de regreso y título "Directorio de Alumnos". Fondo lila muy claro. Cada elemento debe ser una tarjeta horizontal con fotografía o avatar circular a la izquierda, nombre en negrita, carrera en texto pequeño debajo y una flecha a la derecha. Usa:
Juan León — Ingeniería de Sistemas
María García — Arquitectura
Carlos Pérez — Medicina
Ana López — Derecho
Luis Ramírez — Administración

DETAIL:
Diseña esta pantalla como un "Expediente Académico". Usa barra superior blanca con flecha de regreso y título "Expediente Académico". Debajo crea una cabecera morada grande con esquinas inferiores redondeadas. Coloca un avatar circular grande parcialmente superpuesto sobre esa cabecera. Debajo muestra:
"Antonella Quispe"
"Ingeniería de Sistemas"
Después una tarjeta clara con:
"ID Estudiante" — "2024-0001"
"Correo Electrónico" — "antonella.quispe@example.com"
"Facultad" — "Ingeniería y Tecnología"
Agrega una sección "Biografía" con un texto pequeño debajo.

PROFILE:
Diseña esta pantalla como "Configuración de Perfil". Usa barra superior blanca con flecha de regreso y título "Configuración de Perfil". Debajo crea una cabecera con degradado morado y un avatar circular centrado. Debajo muestra "Antonella Quispe". Agrega la sección "INFORMACIÓN PERSONAL" con filas para Nombre Completo, Correo y Teléfono. Luego la sección "ACADÉMICO" con Carrera y Ciclo Actual. Cada fila debe tener icono, etiqueta pequeña y valor. Al final coloca un botón ancho con fondo rosado muy claro, icono de salida y texto "Cerrar Sesión".

Mantén las cuatro pantallas actuales y sus rutas exactamente como están. No agregues login, nuevas pantallas, base de datos ni nuevas funcionalidades. Solo rediseña visualmente la interfaz para que el resultado sea lo más parecido posible al diseño del PDF: morado y lila, tarjetas redondeadas, sombras suaves, avatares circulares, barras superiores limpias, degradados y distribución espaciosa.
