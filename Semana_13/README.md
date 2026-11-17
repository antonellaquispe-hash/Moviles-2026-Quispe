
# CampusGPS — respuestas de reflexión

## Para pensar

### 1. ¿Por qué `FusedLocationProviderClient` está dentro del Repository y no en el ViewModel?

El Repository encapsula el acceso a Google Play Services y al contexto de Android. El ViewModel depende de la interfaz `UbicacionRepository`, así no conoce el SDK concreto ni necesita recibir un `Context`. Esto mantiene las responsabilidades separadas y facilita cambiar o sustituir la fuente de ubicación.

### 2. ¿Qué diferencia hay entre el permiso de ubicación y la clave de API de Maps? ¿Qué pasa si falta cada uno?

El permiso de ubicación lo concede la persona desde Android y permite consultar la posición del dispositivo. La clave de API identifica el uso de Maps SDK for Android y autoriza cargar el mapa. Sin permiso, la app no puede obtener la ubicación, aunque el mapa podría mostrarse. Sin una clave válida, la ubicación todavía puede funcionar, pero Google Maps no podrá cargar correctamente el mapa.

### 3. ¿Por qué la clave va en `local.properties` y no en el código? ¿Qué harías si se subiera a GitHub por error?

`local.properties` está excluido de Git, por lo que la clave no aparece en el repositorio ni en el código fuente. La app la recibe mediante un placeholder del Manifest. Si se subiera por error, restringiría y reemplazaría la clave en Google Cloud, actualizaría `local.properties` y verificaría que la clave anterior ya no pueda usarse. También mantendría las restricciones de aplicación para `com.quispe.campusgps`, la huella SHA-1 y Maps SDK for Android.

### 4. ¿Dónde calculas la distancia y por qué no en el Composable?

La distancia se calcula en `util/Distancia.kt` con `Location.distanceBetween`, y el ViewModel actualiza y ordena la lista cuando obtiene la ubicación. El Composable solo presenta los datos. Así la interfaz no mezcla dibujo con lógica de cálculo y la regla puede reutilizarse independientemente de la pantalla.

### 5. ¿Qué pasa con tu app si el usuario niega el permiso dos veces? ¿Cómo lo manejas?

Después de rechazar varias veces, Android puede dejar de mostrar el diálogo de permiso. La app no debe seguir pidiéndolo indefinidamente: muestra el error de permiso denegado y ofrece **Abrir ajustes** para que la persona pueda habilitarlo manualmente.
