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
        val BLOCKED = listOf("tiktok", "bytedance", "pangle", "snssdk", "douyin")
    }

    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "ShielD7L Seguridad",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("SHIELD7L 🛡️ ACTIVO")
            .setContentText("Monitoreando conexiones...")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true)
            .build()

        startForeground(1001, notification)

        if (!isRunning) {
            isRunning = true
            job = CoroutineScope(Dispatchers.IO).launch {
                while (isRunning) {
                    scanConnections()
                    delay(3000)
                }
            }
        }
        return START_STICKY
    }

    private suspend fun scanConnections() {
        try {
            BufferedReader(FileReader("/proc/net/tcp")).use { reader ->
                reader.lineSequence().drop(1).forEach { line ->
                    val parts = line.split("\\s+".toRegex())
                    if (parts.size >= 10 && parts[3] == "01") {
                        val ipHex = parts[2].split(":")[0]
                        if (ipHex != "00000000" && ipHex != "0100007F") {
                            val ip = hexToIp(ipHex)
                            checkIp(ip)
                        }
                    }
                }
            }
        } catch (_: Exception) {}
    }

    private fun checkIp(ip: String) {
        try {
            val host = InetAddress.getByName(ip).hostName.lowercase()
            for (word in BLOCKED) {
                if (host.contains(word)) {
                    alert("⚠️ CONEXIÓN SOSPECHOSA", "$ip → $host")
                    return
                }
            }
        } catch (_: Exception) {}
    }

    private fun hexToIp(h: String): String {
        if (h.length != 8) return h
        return listOf(6..7, 4..5, 2..3, 0..1)
            .joinToString(".") { h.substring(it).toInt(16).toString() }
    }

    private fun alert(title: String, msg: String) {
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(msg)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(Notification.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        getSystemService(Context.NOTIFICATION_SERVICE)
            .notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onDestroy() {
        isRunning = false
        job?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
