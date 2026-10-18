# CafeCampus — Tecsup

Aplicación Android para la gestión de pedidos de una cafetería universitaria.

## Objetivo

Desarrollar una aplicación móvil que permita a los clientes consultar productos, crear pedidos y revisar su estado, mientras que el personal administrativo puede gestionar el menú y controlar los pedidos.

## Funcionalidades implementadas

### Cliente
- Inicio de sesión y gestión de sesión.
- Consulta del menú de productos.
- Visualización del detalle de cada producto.
- Gestión del carrito y confirmación del pedido.
- Selección de hora de recojo y método de pago.
- Consulta y seguimiento de los pedidos propios.

### Administración
- Panel administrativo.
- Gestión de productos del menú.
- Consulta y seguimiento de pedidos.
- Actualización del estado de los pedidos.
- Cancelación de pedidos cuando el estado lo permite.
- Consulta de información y ajustes de la aplicación.

## Tecnologías

- Kotlin.
- Android Studio.
- Jetpack Compose y Material 3.
- Navigation Compose.
- Room para persistencia local.
- Kotlin Coroutines y Flow.
- ViewModel.
- Gradle.

## Estructura del proyecto

```text
app/src/main/java/com/quispe/cafecampus/
├── data/
│   ├── local/
│   └── repository/
├── model/
├── navigation/
├── ui/
│   ├── admin/
│   ├── cliente/
│   ├── components/
│   └── theme/
├── viewmodel/
└── CafeCampus
