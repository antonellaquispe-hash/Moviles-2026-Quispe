# PROMPTS.md — Fase 2 con IA

En esta sesión se usó Codex como asistente. No se atribuyen respuestas a Gemini.

| Prompt que escribí | Qué respondió la IA (Codex) | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- |
| «Mueve la tarea de progreso desde `TareasViewModel` al Repository con un `Flow<Float>` frío. Emite avances de 0 a 1 cada 40 ms, ejecuta el flujo con `flowOn(Dispatchers.Default)` y conserva la cancelación con `Job`, los estados `Trabajando...`, `Terminó` y `Cancelado`, y la UI actual. Evita bloquear el hilo principal.» | Propuso añadir una función de progreso a `TareasRepository`, implementarla en `TareasRepositoryLocal` con `flow { ... }` y `flowOn(Dispatchers.Default)`, y recoger sus emisiones en el ViewModel dentro de `viewModelScope`. La cancelación del `Job` cancela la colección y el bloque `finally` restablece el estado de ejecución. | Añadí `tareaConProgreso()` a la interfaz y la implementación local; moví el ciclo de 100 emisiones al Repository y dejé que el ViewModel solo recoja los valores para actualizar `StateFlow`. Conservé los registros `HILO` antes, dentro y después del flujo. | El Repository ahora contiene el trabajo que simula la tarea y el ViewModel administra estado y eventos. `flowOn(Dispatchers.Default)` mantiene las emisiones fuera del hilo principal, mientras que la cancelación y los estados observables siguen funcionando. |
