package com.quispe.cafecampus.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.quispe.cafecampus.MainActivity
import com.quispe.cafecampus.R
import com.quispe.cafecampus.model.EstadoPedido

/**
 * Funcion unica de la app: aviso local al estudiante cuando el administrador
 * cambia el estado de su pedido. No necesita servidor: el Repository ya
 * observa la base Room y el Flow entrega el nuevo estado.
 */
object NotificacionesPedido {

    const val CANAL_ID = "pedidos_cafecampus"

    private const val ID_BASE = 4200

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val canal = NotificationChannel(
            CANAL_ID,
            "Estado de mis pedidos",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Avisos cuando cambia el estado de tu pedido"
        }

        context.getSystemService(NotificationManager::class.java)
            ?.createNotificationChannel(canal)
    }

    fun tienePermiso(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun notificarCambioEstado(
        context: Context,
        pedidoId: Int,
        estado: EstadoPedido
    ) {
        if (!tienePermiso(context)) return

        crearCanal(context)

        val intencion = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            pedidoId,
            intencion,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(R.drawable.ic_notification_cafe)
            .setContentTitle("Pedido #$pedidoId: ${estado.etiqueta()}")
            .setContentText(mensaje(estado))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context)
                .notify(ID_BASE + pedidoId, notificacion)
        } catch (excepcion: SecurityException) {
            // El usuario denego el permiso: se ignora el aviso.
        }
    }

    private fun mensaje(estado: EstadoPedido): String {
        return when (estado) {
            EstadoPedido.RECIBIDO -> "Recibimos tu pedido, en breve empezamos."
            EstadoPedido.PREPARANDO -> "Estamos preparando tu pedido."
            EstadoPedido.LISTO -> "Tu pedido esta listo para recoger."
            EstadoPedido.ENTREGADO -> "Tu pedido fue entregado. Que lo disfrutes."
            EstadoPedido.CANCELADO -> "Tu pedido fue cancelado."
        }
    }
}
