package com.quispe.lab02carrito

// =====================================================
// CLASE PADRE
// =====================================================

open class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
) {

    // Método abierto para aplicar polimorfismo
    open fun calcularImporte(): Double {
        return precio * cantidad
    }
}


// =====================================================
// CLASE HIJA: PRODUCTO NORMAL
// =====================================================

class ProductoNormal(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad)


// =====================================================
// CLASE HIJA: PRODUCTO EN OFERTA
// =====================================================

class ProductoOferta(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    // POLIMORFISMO
    // ProductoOferta modifica el comportamiento
    // del método calcularImporte()
    override fun calcularImporte(): Double {
        return precio * cantidad * 0.90
    }
}


// =====================================================
// CALCULAR SUBTOTAL
// =====================================================

fun calcularSubtotal(productos: List<Producto>): Double {

    var subtotal = 0.0

    for (p in productos) {

        // POLIMORFISMO:
        // p puede ser ProductoNormal o ProductoOferta
        subtotal += p.calcularImporte()
    }

    return subtotal
}


// =====================================================
// CALCULAR IGV
// =====================================================

fun calcularIGV(subtotal: Double): Double {

    return subtotal * 0.18
}


// =====================================================
// CALCULAR TOTAL
// =====================================================

fun calcularTotal(
    subtotal: Double,
    igv: Double
): Double {

    return subtotal + igv
}


// =====================================================
// CALCULAR DESCUENTO
// =====================================================

fun calcularDescuento(total: Double): Double {

    return when {

        total > 5000 ->
            total * 0.10

        total > 3000 ->
            total * 0.05

        else ->
            0.0
    }
}


// =====================================================
// MOSTRAR DETALLE
// =====================================================

fun mostrarDetalle(productos: List<Producto>) {

    println()
    println("--------- DETALLE DEL CARRITO ---------")

    var i = 1

    for (p in productos) {

        // POLIMORFISMO
        val importe = p.calcularImporte()

        println(
            String.format(
                "%d. %-20s x%d S/%8.2f",
                i,
                p.nombre,
                p.cantidad,
                importe
            )
        )

        i++
    }

    println("---------------------------------------")
}


// =====================================================
// PROGRAMA PRINCIPAL
// =====================================================

fun main() {

    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP")
    println("=========================================")

    // Pedir nombre del cliente
    print("Ingrese nombre del cliente: ")

    val nombreCliente = readln()

    // Lista de productos
    // Puede almacenar ProductoNormal y ProductoOferta
    val carrito = mutableListOf<Producto>()

    var continuar = true


    // =================================================
    // INGRESAR PRODUCTOS
    // =================================================

    while (continuar) {

        println()
        println("--------- AGREGAR PRODUCTO ---------")

        // Nombre
        print("Ingrese nombre del producto: ")

        val nombre = readln().trim()

        if (nombre.isEmpty()) {

            println("El nombre del producto no puede estar vacío.")

            continue
        }


        // Precio
        print("Ingrese precio: ")

        val precio = readln().toDoubleOrNull()

        if (precio == null || precio <= 0) {

            println("Precio inválido. Debe ser mayor que 0.")

            continue
        }


        // Cantidad
        print("Ingrese cantidad: ")

        val cantidad = readln().toIntOrNull()

        if (cantidad == null || cantidad <= 0) {

            println("Cantidad inválida. Debe ser mayor que 0.")

            continue
        }


        // =================================================
        // CREAR PRODUCTO
        // =================================================

        /*
         * POLIMORFISMO
         *
         * La variable es de tipo Producto,
         * pero puede contener un ProductoNormal
         * o un ProductoOferta.
         */

        val producto: Producto

        if (precio >= 500) {

            producto = ProductoOferta(
                nombre = nombre,
                precio = precio,
                cantidad = cantidad
            )

            println()
            println(
                "Tipo: Producto en oferta"
            )

            println(
                "Se aplicará 10% de descuento al producto."
            )

        } else {

            producto = ProductoNormal(
                nombre = nombre,
                precio = precio,
                cantidad = cantidad
            )

            println()
            println(
                "Tipo: Producto normal"
            )
        }


        // Agregar al carrito
        carrito.add(producto)

        println()
        println("Producto agregado correctamente.")


        // Preguntar si desea continuar
        println()

        print("¿Desea agregar otro producto? (s/n): ")

        val respuesta = readln()
            .trim()
            .lowercase()

        if (respuesta != "s") {

            continuar = false
        }
    }


    // =================================================
    // VALIDAR CARRITO
    // =================================================

    if (carrito.isEmpty()) {

        println()
        println("No se agregaron productos.")

        return
    }


    // =================================================
    // CALCULOS
    // =================================================

    val subtotal =
        calcularSubtotal(carrito)

    val igv =
        calcularIGV(subtotal)

    val total =
        calcularTotal(
            subtotal,
            igv
        )

    val descuento =
        calcularDescuento(total)

    val totalConDescuento =
        total - descuento


    // =================================================
    // MOSTRAR RESULTADO
    // =================================================

    println()

    println("=========================================")
    println(" RESUMEN DEL CARRITO")
    println("=========================================")

    println(
        "Cliente: $nombreCliente"
    )

    // Mostrar productos
    mostrarDetalle(carrito)

    println()

    println(
        "Cantidad de productos: ${carrito.size}"
    )

    println()


    // =================================================
    // PRODUCTO MÁS CARO
    // =================================================

    val masCaro =
        carrito.maxByOrNull {
            it.precio
        }

    if (masCaro != null) {

        println(
            "Producto más caro: ${masCaro.nombre} " +
                    String.format(
                        "(S/%.2f)",
                        masCaro.precio
                    )
        )
    }


    println()


    // =================================================
    // MOSTRAR CALCULOS
    // =================================================

    println(
        String.format(
            "Subtotal: S/%.2f",
            subtotal
        )
    )

    println(
        String.format(
            "IGV (18%%): S/%.2f",
            igv
        )
    )

    println(
        String.format(
            "Total: S/%.2f",
            total
        )
    )


    // =================================================
    // MOSTRAR DESCUENTO
    // =================================================

    if (descuento > 0) {

        println(
            String.format(
                "Descuento adicional: S/%.2f",
                descuento
            )
        )
    }


    println(
        String.format(
            "TOTAL CON DESCUENTO: S/%.2f",
            totalConDescuento
        )
    )


    println()

    println("=========================================")
    println(" Gracias por su compra")
    println("=========================================")
}