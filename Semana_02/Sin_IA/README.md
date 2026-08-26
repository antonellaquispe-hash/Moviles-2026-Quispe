Compras en Kotlin

**Nombre:** Antonella Quispe
**Curso:** Programación en Móviles - 4to Ciclo
**Docente:** Juan José León Suiyon

## Descripción
Este programa simula la lógica de un carrito de compras en consola utilizando Kotlin. 
Implementa una `data class` para modelar productos, funciones para calcular el subtotal, IGV (18%) y total, 
un reporte detallado con columnas alineadas, búsqueda del producto más caro y aplicación de descuentos condicionales usando `when`.

## Captura de la Consola
A continuación se muestra el resultado final del programa en la consola de Android Studio:
<img width="1487" height="785" alt="image" src="https://github.com/user-attachments/assets/9d2f739a-c19a-4210-b91b-cde6fc4b297c" />

## Reflexión: val vs var
**Pregunta:** ¿Por qué `nombre` y `precio` son `val` pero `cantidad` es `var`? ¿Qué pasaría si intentas cambiar el precio después de crear el producto?

**Respuesta:** 
Usé `val` para `nombre` y `precio` porque son características fijas del producto que no deben cambiar una vez creado el objeto. 
Usé `var` para `cantidad` porque el cliente puede agregar más unidades al carrito, por lo que este valor necesita ser mutable. 
Si intentara cambiar el precio después de crear el producto, Kotlin mostraría un error de compilación.
