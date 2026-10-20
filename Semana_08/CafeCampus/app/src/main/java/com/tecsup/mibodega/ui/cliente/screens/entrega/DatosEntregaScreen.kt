package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.componentes.CampoTexto

@Composable
fun DatosEntregaScreen(
    onVolver: () -> Unit,
    onContinuar: (String, Double) -> Unit
) {
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var tipoEntrega by remember { mutableStateOf("Delivery") }
    var intentoContinuar by remember { mutableStateOf(false) }

    val direccionError = intentoContinuar && direccion.isBlank()
    val referenciaError = intentoContinuar && referencia.isBlank()
    val telefonoError = intentoContinuar && telefono.isBlank()

    val formularioCompleto =
        direccion.isNotBlank() &&
                referencia.isNotBlank() &&
                telefono.isNotBlank()

    val costoEntrega = if (tipoEntrega == "Delivery") 4.00 else 0.00

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Datos de entrega",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Completa los datos para recibir tu pedido."
        )

        CampoTexto(
            etiqueta = "Dirección",
            valor = direccion,
            onValorCambia = { direccion = it },
            placeholder = "Ej. Av. Arequipa 123"
        )

        if (direccionError) {
            Text(
                text = "La dirección es obligatoria",
                color = Color.Red
            )
        }

        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = { referencia = it },
            placeholder = "Ej. Frente al parque"
        )

        if (referenciaError) {
            Text(
                text = "La referencia es obligatoria",
                color = Color.Red
            )
        }

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            placeholder = "Ej. 987654321"
        )

        if (telefonoError) {
            Text(
                text = "El teléfono es obligatorio",
                color = Color.Red
            )
        }

        Text(
            text = "Tipo de entrega",
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = tipoEntrega == "Delivery",
                        onClick = {
                            tipoEntrega = "Delivery"
                        }
                    )

                    Column {
                        Text(
                            text = "Delivery",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Entrega a domicilio - S/ 4.00"
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = tipoEntrega == "Recojo en tienda",
                        onClick = {
                            tipoEntrega = "Recojo en tienda"
                        }
                    )

                    Column {
                        Text(
                            text = "Recojo en tienda",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Recoge tu pedido gratis"
                        )
                    }
                }
            }
        }

        Text(
            text = "Costo de entrega: S/ %.2f".format(costoEntrega),
            fontWeight = FontWeight.Bold
        )

        BotonPrimario(
            texto = "Continuar",
            onClick = {
                intentoContinuar = true

                if (formularioCompleto) {
                    onContinuar(tipoEntrega, costoEntrega)
                }
            }
        )

        BotonSecundario(
            texto = "Volver",
            onClick = onVolver
        )
    }
}