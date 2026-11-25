package com.quispe.avisoscampus.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.quispe.avisoscampus.MainActivity
import com.quispe.avisoscampus.R
import com.quispe.avisoscampus.model.Aviso

object Notificador {
    const val CANAL = "avisos"
    const val CANAL_URGENTE = "avisos_urgentes"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canalAvisos = NotificationChannel(
                CANAL,
                "Avisos",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val canalUrgente = NotificationChannel(
                CANAL_URGENTE,
                "Urgentes",
                NotificationManager.IMPORTANCE_HIGH
            )
            val administrador = context.getSystemService(NotificationManager::class.java)
            administrador.createNotificationChannel(canalAvisos)
            administrador.createNotificationChannel(canalUrgente)
        }
    }

    fun tienePermiso(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false

        val intent = Intent(context, MainActivity::class.java)
        val pendiente = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val canal = if (aviso.urgente) CANAL_URGENTE else CANAL
        val prioridad = if (aviso.urgente) {
            NotificationCompat.PRIORITY_HIGH
        } else {
            NotificationCompat.PRIORITY_DEFAULT
        }
        val notificacion = NotificationCompat.Builder(context, canal)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(aviso.texto)
            )
            .setContentIntent(pendiente)
            .setPriority(prioridad)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}
