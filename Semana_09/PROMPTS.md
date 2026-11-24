# Registro de prompts de la fase 2

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Mi criterio técnico |
|---|---|---|---|
| «Traduce la API, pero eso ya en la fase con IA; cambia a la rama mejora-ia, donde hacemos la fase 2 y la traducción». | Propuso traducir los textos recibidos del servicio dentro del repositorio para que lista, detalle y comentarios muestren contenido en español sin modificar los DTO ni trasladar llamadas de red a la interfaz. | Usé ML Kit Translation en el dispositivo en vez de enviar el contenido a un servicio remoto que requiera una clave integrada en la app. Añadí caché en memoria y descarga del modelo solo por Wi-Fi. | El repositorio sigue coordinando el acceso a datos; la cancelación se propaga; no se exponen credenciales. El modelo se descarga la primera vez, por eso se informa el requisito de Wi-Fi si la traducción falla. |
