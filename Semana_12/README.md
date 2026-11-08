# RockVault

Aplicación Android para consultar publicaciones, leer sus detalles y guardar favoritas. El proyecto del laboratorio integra Jetpack Compose, MVVM, Retrofit, Room, Firebase Authentication, Firestore y notificaciones locales.

## Tema y variante

- Tema: Publicaciones.
- Variante: artículos publicados de DEV Community mediante la API pública de Forem.
- Paquete Android: `com.tecsup.rockvault`.

## Funciones

- Inicio de sesión con correo y contraseña.
- Lectura del rol `admin` o `usuario` desde `usuarios/{uid}` en Firestore.
- Lista de publicaciones, detalle y favoritos locales.
- Publicaciones escritas en español.
- Tema claro blanco con morado y modo oscuro negro con morado, con preferencia guardada.
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
5. Según los documentos actuales, el perfil administrador usa el campo `Administrador` con valor `admin`, y el perfil normal usa el campo `Usuario` con valor `usuario`. La app también reconoce el formato `rol` con valor `admin` o `usuario`.
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

- `data/remote`: cliente Retrofit, servicio de artículos DEV/Forem y DTO.
- `data/local`: entidades, DAO y base de datos Room.
- `data/repository`: contrato e implementación del catálogo.
- `data/auth`: contrato e implementación de autenticación Firebase.
- `domain/model`: modelos `Item`, `Sesion` y `Rol`.
- `ui`: pantallas y ViewModels de login, catálogo, detalle, favoritos y administración.
- `notification`: contrato e implementación Android para notificaciones.

Room es la fuente de datos que observa la interfaz. Retrofit consulta publicaciones en español etiquetadas `spanish` con `GET /articles?per_page=30&top=365&tag=spanish`, verifica el idioma `es` y obtiene el contenido completo con `GET /articles/{id}`. La sincronización oculta publicaciones de otros idiomas y conserva los favoritos en español.

## Reparto de tareas

| Área | Trabajo |
| --- | --- |
| Trabajo individual | Estructura MVVM, consulta de artículos DEV/Forem, persistencia Room, acceso Firebase, pantallas, favoritos, panel Admin, notificaciones y documentación |

El laboratorio se desarrolla de manera individual.
