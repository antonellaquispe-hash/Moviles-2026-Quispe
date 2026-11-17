# PROMPTS.md — Fase 2 con IA

Registro de prompts importantes usados para mejorar CampusGPS en la rama `mejora-ia`.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- |
| «Implementa un filtro por categoría para CampusGPS. Los chips Estudio, Comida, Eventos y Servicios deben filtrar los lugares visibles tanto en el mapa como en la lista. Conserva MVVM + Repository y no muevas lógica al Composable.» | Propuso mantener la lista original en el `MapaViewModel`, derivar una lista filtrada según una categoría seleccionada y exponer el estado para que la pantalla dibuje chips y consuma los lugares filtrados. | Añadí el estado de categoría y la lista derivada con `combine`; conservé los cinco lugares en el estado base para que cambiar el filtro no pierda distancias. Al filtrar, limpio la selección si el lugar deja de estar visible. La interfaz solo envía eventos y presenta el estado. | El filtro es la primera mejora de la Fase 2 y respeta el contrato de la guía: ViewModel + View, sin `Context`, Repository nuevo ni reglas de negocio en UI. |
