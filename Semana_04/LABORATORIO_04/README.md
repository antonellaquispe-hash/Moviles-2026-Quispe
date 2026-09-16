# Laboratorio 04 - Carrito de Compras TECSUP

## Descripción

Aplicación Android desarrollada con Kotlin y Jetpack Compose que permite gestionar un carrito de compras.

La aplicación permite:

- Registrar productos con nombre, precio y cantidad.
- Mostrar los productos agregados en una lista.
- Calcular el subtotal.
- Calcular el IGV del 18%.
- Calcular el total de la compra.
- Eliminar productos del carrito.
- Confirmar la eliminación mediante un cuadro de diálogo.
- Aplicar descuentos según el monto total.
- Mostrar un mensaje cuando el carrito está vacío.

## Funcionalidades adicionales

### Confirmación de eliminación

Antes de eliminar un producto se muestra un cuadro de diálogo de confirmación con las opciones **CANCELAR** y **ELIMINAR**.

### Descuento

Se utiliza una expresión `when` para aplicar el descuento:

- Más de S/ 3000 → 5%.
- Más de S/ 5000 → 10%.
- Hasta S/ 3000 → sin descuento.

## Resultado

<img width="253" height="565" alt="image" src="https://github.com/user-attachments/assets/9185326c-c578-4f1b-99f6-a3aa0dd36a4d" />

<img width="262" height="579" alt="image" src="https://github.com/user-attachments/assets/f37e2668-0bf0-4a9f-9f18-917a988a7023" />


## Preguntas conceptuales

### 1. ¿Qué es el estado en Jetpack Compose?

El estado es la información que puede cambiar durante la ejecución de la aplicación y que permite actualizar automáticamente la interfaz cuando cambia su valor.

### 2. ¿Para qué sirve remember y mutableStateOf?

`remember` permite conservar un valor durante las recomposiciones de Compose.

`mutableStateOf` crea un estado observable. Cuando su valor cambia, Compose actualiza la interfaz relacionada con ese estado.

### 3. ¿Por qué se utiliza mutableStateListOf para los productos?

`mutableStateListOf` permite mantener una lista observable de productos. Cuando se agrega o elimina un producto, Compose detecta el cambio y actualiza automáticamente la lista mostrada en pantalla.
