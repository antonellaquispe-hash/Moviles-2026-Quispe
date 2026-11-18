
# CampusGPS — respuestas de reflexión

La rama `mejora-ia` usa MapLibre Compose y el estilo Liberty de OpenFreeMap, con datos de OpenStreetMap y atribución incluida en el mapa.

## Para pensar

### 1. ¿Por qué `FusedLocationProviderClient` está dentro del Repository y no en el ViewModel?

El Repository encapsula el acceso a Google Play Services y al contexto de Android. El ViewModel depende de la interfaz `UbicacionRepository`, así no conoce el SDK concreto ni necesita recibir un `Context`. Esto mantiene las responsabilidades separadas y facilita cambiar o sustituir la fuente de ubicación.

### 2. ¿Qué diferencia hay entre el permiso de ubicación y el mapa? ¿Qué pasa si falta cada uno?

El permiso de ubicación lo concede la persona desde Android y permite consultar la posición del dispositivo. El mapa usa MapLibre Compose con un estilo basado en datos de OpenStreetMap, así que no necesita una clave de Google Maps. Sin permiso, la app no puede obtener ni mostrar la ubicación actual, aunque el mapa y los lugares del campus pueden mostrarse. Sin conexión a Internet, el mapa base no podrá cargar sus calles.

### 3. ¿Por qué la clave va en `local.properties` y no en el código? ¿Qué harías si se subiera a GitHub por error?

`local.properties` está excluido de Git y puede guardar configuraciones locales. Esta versión del mapa no consume una clave de Google Maps. Si una clave anterior se subió por error, la restringiría y reemplazaría en Google Cloud, y verificaría que la anterior ya no pueda usarse.

### 4. ¿Dónde calculas la distancia y por qué no en el Composable?

La distancia se calcula en `util/Distancia.kt` con `Location.distanceBetween`, y el ViewModel actualiza y ordena la lista cuando obtiene la ubicación. El Composable solo presenta los datos. Así la interfaz no mezcla dibujo con lógica de cálculo y la regla puede reutilizarse independientemente de la pantalla.

### 5. ¿Qué pasa con tu app si el usuario niega el permiso dos veces? ¿Cómo lo manejas?

Después de rechazar varias veces, Android puede dejar de mostrar el diálogo de permiso. La app no debe seguir pidiéndolo indefinidamente: muestra el error de permiso denegado y ofrece **Abrir ajustes** para que la persona pueda habilitarlo manualmente.
