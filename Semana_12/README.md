# RockVault

Aplicación Android para buscar álbumes de rock, consultar sus detalles y guardar favoritos. El proyecto del laboratorio integra Jetpack Compose, MVVM, Retrofit, Room, Firebase Authentication, Firestore y notificaciones locales.

## Tema y variante

- Tema: álbumes musicales.
- Variante: búsqueda de álbumes de rock con iTunes Search API, usando `term=rock` y `entity=album`.
- Paquete Android: `com.tecsup.rockvault`.

## Funciones

- Inicio de sesión con correo y contraseña.
- Lectura del rol `admin` o `usuario` desde `usuarios/{uid}` en Firestore.
- Catálogo de álbumes, detalle y favoritos locales.
- Datos guardados con Room para consultar el catálogo sin conexión.
- Acceso al panel de administración solo para cuentas con rol `admin`.
- Totales de elementos y favoritos, sincronización y limpieza de caché en el panel.
- Notificación local al completar una sincronización.

## Requisitos

- Android Studio con Android SDK 37.
- JDK 11 o superior.
- Un emulador con Google Play o un dispositivo Android con conexión a internet.
- El archivo `app/google-services.json` correspondiente al proyecto Firebase y al paquete `com.tecsup.rockvault`.
- Authentication con el método Correo electrónico/contraseña habilitado y Firestore configurado.

## Compilar e instalar

Desde esta carpeta ejecuta en PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug
```

El APK de depuración queda en `app/build/outputs/apk/debug/app-debug.apk`. Para instalarlo con un dispositivo conectado y con depuración USB habilitada:

```powershell
.\gradlew.bat :app:installDebug
```

## Configurar usuarios de prueba

1. En Firebase Console, abre Authentication y habilita el proveedor Correo electrónico/contraseña.
2. Crea una cuenta de administrador y una cuenta de usuario. Usa las credenciales de prueba de tu equipo y no las guardes en este repositorio.
3. En Firestore, crea la colección `usuarios`.
4. Para cada cuenta, crea un documento cuyo ID sea exactamente el UID que aparece en Authentication.
5. Agrega los campos `nombre` (texto) y `rol` (texto). Usa `admin` para la cuenta administradora y `usuario` para la cuenta normal.
6. Confirma que el archivo Firebase esté dentro de `app/` y corresponda al mismo proyecto y paquete Android.

Reglas sugeridas para que cada persona autenticada lea su propio perfil:

```text
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

## Arquitectura

- `data/remote`: cliente Retrofit, servicio iTunes y DTO.
- `data/local`: entidades, DAO y base de datos Room.
- `data/repository`: contrato e implementación del catálogo.
- `data/auth`: contrato e implementación de autenticación Firebase.
- `domain/model`: modelos `Item`, `Sesion` y `Rol`.
- `ui`: pantallas y ViewModels de login, catálogo, detalle, favoritos y administración.
- `notification`: contrato e implementación Android para notificaciones.

Room es la fuente de datos que observa la interfaz. Retrofit actualiza los registros guardados y conserva los favoritos durante la sincronización.

## Capturas

Las capturas deben tomarse después de iniciar sesión con las cuentas de prueba y cargar álbumes, para que las vistas de catálogo y detalle muestren las carátulas reales. Guarda las capturas verificadas en `docs/capturas/` con estos nombres:

- `login.png`
- `catalogo.png` (con carátulas visibles)
- `detalle.png` (con información del álbum)
- `favoritos.png` (con al menos un álbum marcado)
- `panel-admin.png` (con la cuenta administradora)
- `notificacion.png` (notificación de sincronización)

No se incluyen capturas vacías ni imágenes de muestra. Añade las capturas obtenidas en un dispositivo o emulador con Firebase y sus cuentas de prueba configurados.

## Reparto de tareas

| Área | Trabajo |
| --- | --- |
| Aplicación y datos | Estructura MVVM, consulta iTunes, persistencia Room, detalle y favoritos |
| Acceso y administración | Firebase Authentication, perfiles y roles Firestore, navegación y panel Admin |
| Interfaz y entrega | Pantallas Compose, estados, notificaciones, documentación y validación en dispositivo |

El equipo debe asociar estas áreas con los nombres de sus integrantes al preparar la entrega.
