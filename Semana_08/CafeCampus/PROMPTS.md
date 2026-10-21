# Prompts utilizados — Fase 2 (CON IA) · CafeCampus

Rama: `mejora-ia`. Aplicación: Kotlin + Jetpack Compose + Material 3 + MVVM + Room,
con un `CafeRepository` (interface) como contrato único entre la capa de datos y los
ViewModels.

> **Condición obligatoria respetada en todas las mejoras:** la View no usa DAO ni
> Repository; el ViewModel solo conoce la interface `CafeRepository`; toda función
> nueva se declara primero en la interface y luego se implementa en
> `CafeRepositoryRoom`.

---

## Prompt 1 — Buscar en el menú

**Texto enviado a Gemini:**
> Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un CafeRepository
> (interface). Agrega una barra de busqueda en MenuScreen que filtre productos por
> nombre. La logica del filtro debe ir en MenuViewModel (combine de StateFlow), NO en el
> Composable. No uses XML. Muestrame solo los archivos que cambian.

**Que genero la IA:**
- `MenuViewModel`: un `MutableStateFlow<String>` para el texto, un
  `MutableStateFlow<String>` para la categoría y un `productosFiltrados` que combina
  `productos`, `busqueda` y `categoria` con `combine`.
- `MenuScreen`: el `OutlinedTextField` de búsqueda, los `FilterChip` de categoría y el
  `LazyVerticalGrid` observing `productosFiltrados`.
- `MenuViewModel.actualizarBusqueda()`, `seleccionarCategoria()` y `limpiarBusqueda()`.

**Que tuve que corregir yo:**
- La primera versión dejaba el `filter` dentro de un `remember { ... }` del
  Composable (`MenuScreen`), es decir la lógica de filtrado en la View. Se movió
  completa al ViewModel con `combine` de tres `StateFlow`, tal como pide el enunciado.
- Faltaba el botón para limpiar la búsqueda (la `X` del `trailingIcon`).
- Se corrigió la navegabilidad de los chips: antes cambiaban una variable local de la
  pantalla, ahora llaman a `vm.seleccionarCategoria(...)`.

**Respeta MVVM?:** Sí. La View solo observa `StateFlow` y avisa clics; el filtrado vive
en `MenuViewModel`, que solo conoce la interface `CafeRepository`.

---

## Prompt 2 — Repetir pedido desde el historial

**Texto enviado a Gemini:**
> En CafeCampus (MVVM + Room), agrega un boton "Repetir pedido" en la pestana
> Historial de MisPedidosScreen. El boton debe volver a cargar al carrito los productos
> de un pedido anterior. La logica va en MenuViewModel; la View solo avisa el clic.
> No uses XML. Respeta que el ViewModel solo conoce CafeRepository.

**Que genero la IA:**
- `MenuViewModel.repetirPedido(pedido: PedidoConDetalle): Int` que recorre
  `pedido.detalles`, busca cada `productoId` en el menú actual y llama a `agregar(...)`
  ignorando los productos que ya no existen o están agotados.
- `PedidoCard` con un parámetro opcional `onRepetir: (() -> Unit)?` que pinta el botón
  "Repetir pedido" cuando se le entrega.
- `MisPedidosScreen` con el parámetro `onRepetir` y el botón solo en la pestaña
  Historial.
- `NavCafe` conecta `menuViewModel.repetirPedido(pedido)` y navega a
  `CONFIRMAR_PEDIDO`.

**Que tuve que corregir yo:**
- La IA propuso que `PedidoCard` receive el `PedidoConDetalle` y buscara solo el
  producto: se corrigió para que el componente sea tonto y solo reciba el callback
  (la View no debe decide la logica).
- Se corrigió que el botón aparezca únicamente en la pestaña Historial (en Activos el
  pedido todavía está en curso).
- Se documentó en el KDoc que los productos agotados o eliminados se omiten, para que
  el karma del carrito no difiera de lo que ve el estudiante.

**Respeta MVVM?:** Sí. `MenuViewModel` arma el carrito; `MisPedidosScreen` y `PedidoCard`
solo pintan el botón y avisan el clic.

---

## Prompt 3 — Gráfico real de pedidos por hora

**Texto enviado a Gemini:**
> El grafico "Pedidos por hora" del PanelScreen del administrador de CafeCampus
> (MVVM + Room) debe calcularse con una consulta Room, no con datos fijos. Agrega la
> funcion en PedidoDao, declara primero en la interface CafeRepository, implementa en
> CafeRepositoryRoom, y consume en PanelAdminViewModel. La consulta debe agrupar por
> hora y excluir los pedidos cancelados.

**Que genero la IA:**
- `PedidoDao.observarPedidosPorHora(inicio)` con
  `strftime('%H', fecha / 1000, 'unixepoch', 'localtime')`, `GROUP BY hora` y
  `estado != 'CANCELADO'`.
- `model/PedidoPorHora(hora: Int, cantidad: Int)`.
- `CafeRepository.observarPedidosPorHora(inicioDia)` y su implementación en
  `CafeRepositoryRoom`.
- `PanelAdminViewModel.pedidosPorHora` con `map` para completar las horas sin pedidos.

**Que tuve que corregir yo:**
- La IA propuso `CAST(strftime(...) AS INTEGER)` solo en la versión desktop: se dejó el
  `CAST` en la consulta para que Room associe la columna a `Int` y no a `String`.
- Se corrigió el `GROUP BY`: la primera versión agrupaba por la expresión completa y
  Room no reconocía la columna `hora`; ahora se agrupa por el alias.
- Se corrigió que `SUM(cantidad)` devuelve `Long` en SQLite y provocaba
  `CURSOR_MISMATCH` en Room: se agregó `CAST(SUM(...) AS INTEGER)`.
- Se dejó el `map` en el ViewModel (no en la View) para nofill el gráfico con ceros
  desde la capa de presentación.

**Respeta MVVM?:** Sí. La función se declaró primero en la interface
`CafeRepository`, luego se implementó en `CafeRepositoryRoom` y el ViewModel solo la
consume.

---

## Prompt 4 — Registro de estudiante

**Texto enviado a Gemini:**
> Agrega a CafeCampus una pantalla de registro de estudiante (MVVM + Room): nombre,
> correo institucional, contrasena y confirmacion. El correo debe ser unico. La
> validacion va en un RegistroViewModel que solo conoce la interface CafeRepository; la
> View solo muestra los campos y los errores. No uses XML.

**Que genero la IA:**
- `UsuarioDao.existePorCorreo(correo)` para validar el correo único.
- `CafeRepository.crearUsuario(usuario)` y `existeUsuarioPorCorreo(correo)`.
- `RegistroViewModel` con validaciones de campos vacíos, formato de correo, longitud
  mínima de la contraseña y coincidencia de la confirmación.
- `RegistroScreen` con el mismo estilo visual del login y `NavCafe` con la ruta
  `registro` enlazada desde el texto "Regístrate" del login.

**Que tuve que corregir yo:**
- La IA propuso registrar al usuario con un `Index` único sobre `correo` en la entidad
  `Usuario`: eso obliga a subir la versión del esquema y a escribir una `Migration`,
  y el enunciado no lo pide. Se corrigió validando la unicidad en la capa de datos con
  una consulta, sin tocar el esquema.
- Se corrigió el orden de las validaciones: la unicidad del correo se comprueba dentro
  de la corrutina (es una consulta a la base), no antes de entrar en ella.
- Se corrigió el uso de `Rol.CLIENTE.name` para que la cuenta creada siempre sea de
  estudiante y no pueda pedir permisos de administrador.

**Respeta MVVM?:** Sí. `RegistroViewModel` valida y llama al `CafeRepository`; la
`RegistroScreen` no toca ni el DAO ni el Repository.

---

## Prompt 5 — Reporte del día

**Texto enviado a Gemini:**
> En el panel del administrador de CafeCampus agrega una pantalla "Reporte del dia" con
> el producto mas vendido y el total por forma de pago. Los datos deben salir de
> consultas Room (DAO), pasar por la interface CafeRepository y llegar al
> ReporteAdminViewModel. No uses XML.

**Que genero la IA:**
- `PedidoDao.observarProductoMasVendido(inicio)` con `INNER JOIN` entre
  `detalle_pedido` y `pedidos`, `GROUP BY d.nombreProducto` y `LIMIT 1`.
- `PedidoDao.observarTotalesPorFormaPago(inicio)` con `GROUP BY p.pago`.
- `model/ReporteDelDia`, `ProductoMasVendido` y `TotalPorFormaPago`.
- `CafeRepository.observarReporteDelDia(inicioDia)` implementado con `combine` de las
  dos consultas.
- `ReporteAdminViewModel` y `ReporteScreen` con barras por forma de pago.

**Que tuve que corregir yo:**
- La IA propuso calcular el "total vendido" en la View sumando la lista: se corrigió
  para que el total venga del Repository, en el `ReporteDelDia`.
- Se corrigió que el `LEFT JOIN` de la primera versión incluía pedidos cancelados en el
  producto más vendido: se cambió a `INNER JOIN` con el filtro `estado != 'CANCELADO'`.
- Se corrigió el `COALESCE(SUM(p.total), 0.0)` para que devuelva `0.0` y no `null`
  cuando no hay ventas.

**Respeta MVVM?:** Sí. La interface se declaró primero y el ViewModel solo consume el
`Flow` del Repository.

---

## Prompt 6 — Animaciones y estados vacíos

**Texto enviado a Gemini:**
> En CafeCampus agrega animaciones y estados vacios: transiciones entre pantallas,
> animacion del stepper de seguimiento del pedido y mensajes "No hay pedidos" con
> ilustracion. Todo esto es capa View, no toques los ViewModels ni el Repository.
> No uses XML.

**Que genero la IA:**
- `ui/components/EstadoVacio.kt` reusable, con ilustración animada (escala y opacidad
  infinitas), título y mensaje.
- `AnimatedContent` con `fadeIn` + `slideInVertically` alrededor del `StepperEstado` en
  `SeguimientoScreen`.
- `AnimatedVisibility` para el aviso "Cafetería cerrada" de `MenuScreen`.
- `Modifier.animateItem()` en la lista de `MisPedidosScreen` y
  `Modifier.animateContentSize()` en `PedidoCard` y `ProductoCard`.

**Que tuve que corregir yo:**
- La IA propuso modificar `StepperEstado` (componente entregado en el proyecto base).
  Se corrigió: el componente entregado no se toca y la animación se aplica en
  `SeguimientoScreen`, que es la View.
- Se corrigieron los estados vacíos que seguían mostrando un `Text` plano: ahora usan
  `EstadoVacio` con ilustración en el menú, mis pedidos, pedidos del administrador,
  gestión del menú y panel.

**Respeta MVVM?:** Sí. Solo se tocó la capa View, como pedía el enunciado.

---

## Prompt 7 — Aviso local del cambio de estado (función única de la app)

**Texto enviado a Gemini:**
> Agrega a CafeCampus una funcion util: cuando el administrador cambia el estado de un
> pedido, el estudiante recibe un aviso local en el dispositivo aunque no este mirando
> esa pantalla. Debe funcionar sin servidor, apoyandose en el Flow de Room. La
> notificacion se dispara desde la capa View, no desde el Repository.

**Que genero la IA:**
- `util/NotificacionesPedido.kt`: canal de notificaciones, permiso
  `POST_NOTIFICATIONS` y el mensaje según el estado.
- `ui/notificaciones/AvisoEstadoPedido.kt`: un `LaunchedEffect` que compara el estado
  actual de cada pedido con el anterior y dispara el aviso solo cuando cambió.
- `MainActivity`: solicitud del permiso en runtime a partir de Android 13.
- `AndroidManifest.xml`: `<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />`.
- `res/drawable/ic_notification_cafe.xml`: ícono del aviso.

**Que tuve que corregir yo:**
- La IA propuso notificar desde `PedidosClienteViewModel`, lo que metía un `Context` de
  Android en el ViewModel. Se corrigió: la comparación de estados y el disparo del
  aviso viven en la capa View (`AvisoEstadoPedido`), que recibe la lista ya observada.
- Se corrigió que el aviso se disparara en la primera composición: se guarda el estado
  anterior en un `mutableStateMapOf` y solo se notifica si hay un cambio real.
- Se corrigió el `SecurityException` al notificar sin permiso: se captura y se ignora.

**Respeta MVVM?:** Sí. El ViewModel sigue exponiendo solo `StateFlow`; la notificación
es una consecuencia de la View.

---

## Reflexión (5 líneas)

1. **Qué mejoró la IA y qué no:** acertó al proponer la separación de capas (DAO →
   interface → Repository → ViewModel → View) y los nombres de las consultas; falló al
   respetar el "no tocar los componentes entregados" y al meter lógica de filtrado en
   la View.
2. **Qué error cometió:** quiso modificar la entidad `Usuario` para agregar un índice
   único sin pensar en la migración de esquema, y propuso el filtrado dentro del
   Composable cuando el enunciado exigía `combine` de `StateFlow` en el ViewModel.
3. **Qué aprendí sobre MVVM y Room:** en Room, `SUM` devuelve `Long` y `GROUP BY` por
   alias no se reconoce; por eso hay que castear (`CAST(SUM(...) AS INTEGER)`) y leer
   bien el tipo de la columna.
4. **Qué aprendí sobre MVVM y Room:** toda función nueva tiene que declararse primero
   en la interface del Repository; es la línea que separa "el enunciado lo pide" de
   "yo lo invento".
5. **Conclusión:** la IA sirve como borrador de la estructura, pero cada commit hay que
   revisarlo contra el contrato del laboratorio, no solo contra que compile.
