package com.shield7l
import android.app.*
import android.content.Context
import android.os.*
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileReader
import java.net.InetAddress

class SecurityMonitorService : Service() {
    companion object {
        const val CHANNEL_ID = "ShielD7L"
        var isRunning = false
            private set
        val DOMINIOS_BLOQUEADOS = listOf("tiktok", "bytedance", "pangle", "snssdk", "douyin")
    }

    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel.Builder(CHANNEL_ID, "ShielD7L Seguridad", NotificationManager.IMPORTANCE_HIGH)
                .build().also {
                    getSystemService(NotificationManager::class.java).createNotificationChannel(it)
                }
        }
    }

    override fun onStartCommand(i: Intent?, f: Int, startId: Int): Int {
        startForeground(1001, Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("SHIELD7L 🛡️ ACTIVO")
            .setContentText("Monitoreando conexiones...")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true).build())

        if (!isRunning) {
            isRunning = true
            job = CoroutineScope(Dispatchers.IO).launch {
                while (isRunning) {
                    escanearConexiones()
                    delay(3000)
                }
            }
        }
        return START_STICKY
    }

    private suspend fun escanearConexiones() {
        try {
            BufferedReader(FileReader("/proc/net/tcp")).use { reader ->
                reader.lineSequence().drop(1).forEach { linea ->
                    val partes = linea.split("\\s+".toRegex())
                    if (partes.size >= 10 && partes[3] == "01") {
                        val ipHex = partes[2].split(":")[0]
                        if (ipHex != "00000000" && ipHex != "0100007F") {
                            val ip = hexAIp(ipHex)
                            verificarIP(ip)
                        }
                    }
                }
            }
        } catch (_: Exception) {}
    }

    private fun verificarIP(ip: String) {
        try {
            val host = InetAddress.getByName(ip).hostName.lowercase()
            for (palabra in DOMINIOS_BLOQUEADOS) {
                if (host.contains(palabra)) {
                    notificarAlerta("⚠️ CONEXIÓN SOSPECHOSA", "$ip → $host")
                    return
                }
            }
        } catch (_: Exception) {}
    }

    private fun hexAIp(h: String): String {
        if (h.length != 8) return h
        return listOf(6..7, 4..5, 2..3, 0..1)
            .joinToString(".") { h.substring(it).toInt(16).toString() }
    }

    private fun notificarAlerta(titulo: String, mensaje: String) {
        val notif = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(Notification.PRIORITY_HIGH)
            .setAutoCancel(true).build()
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            .notify(System.currentTimeMillis().toInt(), notif)
    }

    override fun onDestroy() {
        isRunning = false
        job?.cancel()
        super.onDestroy()
    }

    override fun onBind(i: Intent?): IBinder? = null
}
