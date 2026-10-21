# CafeCampus — Tecsup

**Programación en Móviles · Laboratorio MVVM + Room (CafeCampus)**

Aplicación Android para la gestión de pedidos de la cafetería del campus. Contiene
dos perfiles que conviven en el mismo dispositivo y comparten la misma base de datos
Room: el **Estudiante** pide y sigue su pedido, y el **Administrador** gestiona los
pedidos y el menú. Al compartir la base, cuando el administrador cambia el estado de
un pedido el estudiante lo ve reflejado en su seguimiento.

## Datos de prueba

| Perfil | Correo | Contraseña |
|---|---|---|
| Estudiante | `juan@campus.test` | `1234` |
| Administrador | `admin@cafe.test` | `admin` |

## Tecnologías

- Kotlin + Jetpack Compose + Material 3 (sin XML).
- Room 2.6.1 (persistencia local) con DAOs, `Flow` y `suspend`.
- DataStore (preferencias: perfil activo, modo oscuro, recepción de pedidos).
- Navigation Compose (grafo de navegación por perfil).
- MVVM + Repository (interface) + StateFlow.

## Arquitectura MVVM + Repository

```text
VIEW (Composable)
 │ eventos (clics)            ▲ collectAsState()
 ▼                            │
VIEWMODEL (StateFlow)         │ Flow / resultados
 │ llama                       │
 ▼                            │
CafeRepository (interface) ───┘
 │ implementa
 ▼
CafeRepositoryRoom ──► DAO ──► SQLite (cafecampus.db)
DataStore (solo preferencias) ──► ViewModel
```

Reglas aplicadas:

- La **View** solo conoce a su ViewModel; nunca al DAO ni al Repository.
- El **ViewModel** solo conoce al Repository (interface); nunca a los Composables ni al DAO.
- El **Repository** conoce los DAOs; no expone entidades de UI ni usa Context de pantalla.
- **Lectura que actualiza la pantalla → `Flow`. Escritura o acción única → `suspend`.**

## Mejoras de la Fase 2 (CON IA)

| Mejora | Qué hace | Capas |
|---|---|---|
| Buscar en el menú | Barra de búsqueda que filtra por nombre mientras escribes | ViewModel + View |
| Repetir pedido | Botón en el historial que vuelve a cargar el carrito | ViewModel + View |
| Gráfico real por hora | El panel agrupa los pedidos con una consulta Room | DAO + Repository + ViewModel + View |
| Registro de estudiante | Pantalla de cuenta nueva con validaciones y correo único | DAO + Repository + ViewModel + View |
| Animaciones y estados vacíos | Stepper animado, transiciones e ilustraciones | View |
| Reporte del día | Producto más vendido y total por forma de pago | DAO + Repository + ViewModel + View |

### Aviso local de cambio de estado

Función propia de la app: cuando el administrador cambia el estado de un pedido, el
estudiante recibe un aviso local en el dispositivo. No necesita servidor: el
`Flow` de Room ya notifica el cambio y la capa View (`AvisoEstadoPedido`) dispara la
notificación comparando el estado actual con el anterior. Requiere el permiso
`POST_NOTIFICATIONS` en Android 13 o superior.

## Estructura del proyecto

```text
Semana_08/CafeCampus/
├── app/src/main/java/com/quispe/cafecampus/
│   ├── MainActivity.kt
│   ├── CafeApp.kt
│   ├── data/
│   │   ├── local/
│   │   │   ├── CafeDatabase.kt
│   │   │   ├── DatosIniciales.kt
│   │   │   ├── entity/ (Producto, Usuario, Pedido, DetallePedido)
│   │   │   ├── dao/ (ProductoDao, UsuarioDao, PedidoDao)
│   │   │   └── relation/ (PedidoConDetalle)
│   │   ├── prefs/ (PreferenciasStore)
│   │   └── repository/ (CafeRepository, CafeRepositoryRoom)
│   ├── model/ (Rol, EstadoPedido, ItemCarrito)
│   ├── viewmodel/
│   │   ├── CafeViewModelFactory.kt
│   │   ├── SesionViewModel.kt
│   │   ├── MenuViewModel.kt
│   │   ├── PedidosClienteViewModel.kt
│   │   ├── PanelAdminViewModel.kt
│   │   ├── PedidosAdminViewModel.kt
│   │   └── MenuAdminViewModel.kt
│   └── ui/
│       ├── theme/ (Color, Type, Theme)
│       ├── components/ (AppTopBar, CafeButton, CafeTextField, CafeBottomBar,
│       │                EstadoChip, ProductoCard, ProductoIcono, PedidoCard, StepperEstado,
│       │                EstadoVacio)
│       ├── navigation/ (Rutas, NavCafe, ItemsNavegacion)
│       ├── notificaciones/ (AvisoEstadoPedido)
│       ├── cliente/ (SplashScreen, LoginScreen, RegistroScreen, MenuScreen,
│       │              DetalleProductoScreen, ConfirmarPedidoScreen, SeguimientoScreen,
│       │              MisPedidosScreen, PerfilScreen)
│       └── admin/ (PanelScreen, PedidosAdminScreen, DetallePedidoAdminScreen,
│                   MenuAdminScreen, FormularioProductoScreen, AjustesScreen,
│                   ReporteScreen)
├── util/ (NotificacionesPedido)
└── gradle/libs.versions.toml
```

## Diseño

La UI sigue Material 3 con la paleta del laboratorio y usa siempre
`MaterialTheme.colorScheme.primary` (nunca colores sueltos).

| Uso | Cliente | Administrador |
|---|---|---|
| Color primario | Naranja `#EA580C` | Índigo `#4F46E5` |
| Contenedor | `#FFEDD5` | `#E0E7FF` |
| Fondo | `#FFFBF7` | `#F7F7FD` |

- `CafeTheme(rol)` entrega el `ColorScheme` según el perfil activo.
- Scaffold con `NavigationBar`: 3 ítems en cliente (Menú, Pedidos, Perfil) y 4 en
  administrador (Panel, Pedidos, Menú, Ajustes).
- `FilterChip` para categorías y estados, `LazyColumn`/`LazyVerticalGrid` para listas y
  `FloatingActionButton` en la gestión del menú.
- Modo oscuro con `darkColorScheme`, activable desde Ajustes y guardado en DataStore.

## Capturas

> Reemplazar estas referencias por las imágenes de la app en ejecución.

### Perfil Estudiante

1. `capturas/cliente-login.png` — Ingreso con selector de rol.
2. `capturas/cliente-menu.png` — Menú con buscador, categorías y "Populares hoy".
3. `capturas/cliente-seguimiento.png` — Seguimiento del pedido con su estado.

### Perfil Administrador

1. `capturas/admin-panel.png` — Panel de control con contadores y pedidos por hora.
2. `capturas/admin-pedidos.png` — Pedidos por estado.
3. `capturas/admin-menu.png` — Gestión del menú.

## Para pensar

**¿Por qué el Repository es una interface?**
Para que el ViewModel dependa del contrato y no de la implementación: el Repository
podrá pasar a hablar con un servicio web o con la nube sin tocar ViewModels ni pantallas.

**¿Por qué crearPedido es suspend y observarPedidosCliente es Flow?**
Porque una escritura ocurre una sola vez (crear el pedido) y una lectura debe seguir
notificando cambios; `Flow` emite de forma continua cuando la tabla cambia.

**¿Qué pasaría si dos perfiles usaran bases de datos distintas?**
El estudiante no vería los cambios de estado que hace el administrador, y el laboratorio
se quedaría sin la observación en tiempo real entre ambos perfiles.

**¿Qué debería cambiar si mañana los datos viven en un servidor?**
Solo la implementación del Repository (`CafeRepositoryRoom`): las APIs de red reemplazarían
a los DAOs, y los ViewModels, las pantallas y el tema se quedarían igual.
