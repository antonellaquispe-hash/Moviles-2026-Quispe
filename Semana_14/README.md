
# CentroTareas

## Observación del Paso 1

Al pulsar **Tarea larga (MAL)**, la pantalla deja de responder durante cuatro segundos: no se pueden tocar los botones ni actualizar el contador. El texto **Trabajando...** no llega a verse mientras dura la espera porque `Thread.sleep(4000)` bloquea el hilo principal, que también debe procesar los toques y dibujar la interfaz. Cuando termina la espera, el hilo principal vuelve a trabajar y Compose muestra **Terminó**.

Si la espera durara diez segundos, Android probablemente detectaría que la aplicación no responde y podría mostrar el diálogo ANR. El tiempo exacto depende del dispositivo y de la operación que esté bloqueando el hilo.
