# Prompts usados en la Fase 2 con Codex

Este registro resume las solicitudes y decisiones trabajadas durante la Fase 2. El asistente utilizado en esta sesión fue Codex; no se atribuyen estas respuestas a Gemini.

| Prompt que escribí (resumen) | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Agregar un canal de notificaciones para avisos urgentes y usarlo para los recordatorios. | Se propuso crear un segundo canal con importancia alta y marcar los recordatorios como urgentes desde el ViewModel. | Se conservaron los dos canales: `avisos` para avisos normales y `avisos_urgentes` para recordatorios. | Android permite que el usuario configure por separado el sonido y la prioridad de cada canal. |
| Hacer que el contenido de las notificaciones se pueda leer completo al expandirlas y mejorar el texto del aviso inmediato. | Se propuso aplicar `BigTextStyle` y usar un mensaje más descriptivo. | Se aplicó `BigTextStyle` al texto de cada aviso y se amplió el texto del aviso inmediato. | Así el contenido largo permanece disponible en la notificación expandida. |
| Agregar un receptor que detecte cuando se conecta o desconecta el cargador, con un control para activar esa escucha. | Se propuso un `BroadcastReceiver` dinámico y un control en la pantalla para iniciar o detener la escucha. | Se agregó el origen `CARGADOR` al historial y se registra el receptor solo mientras la opción está activa; se desregistra al salir de la pantalla. | El control permite elegir cuándo escuchar los eventos y evita mantener el receptor registrado fuera de la pantalla. |
| Compilar el proyecto después de integrar las mejoras. | La compilación de `:app:assembleDebug` finalizó correctamente después de completar las tres mejoras. | Se corrigió el manejo visual del nuevo origen en el historial antes de compilar otra vez. | La compilación verifica que el código Android integra correctamente los cambios. |
