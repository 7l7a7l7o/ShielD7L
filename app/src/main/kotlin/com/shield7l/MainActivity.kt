package com.shield7l

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var status: TextView
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setBackgroundColor(0xFF000000.toInt())
        layout.setPadding(48, 96, 48, 48)

        val title = TextView(this)
        title.setTextColor(0xFFFFFFFF.toInt())
        title.textSize = 28f
        title.text = """
SHIELD7L
SISTEMA DE SEGURIDAD

Titular: J.E.Y.M.7
Versión: 1.1
        """.trimIndent()
        layout.addView(title)

        status = TextView(this)
        status.setTextColor(0xFFCCCCCC.toInt())
        status.textSize = 18f
        status.setPadding(0, 32, 0, 32)
        layout.addView(status)

        btn = Button(this)
        btn.setOnClickListener { toggleService() }
        layout.addView(btn)

        setContentView(layout)
        updateUI()
    }

    private fun toggleService() {
        val intent = Intent(this, SecurityMonitorService::class.java)
        if (SecurityMonitorService.isRunning) {
            stopService(intent)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
        updateUI()
    }

    private fun updateUI() {
        if (SecurityMonitorService.isRunning) {
            status.text = "✅ PROTECCIÓN ACTIVA\nEscaneo cada 3 segundos"
            btn.text = "DETENER PROTECCIÓN"
            btn.setBackgroundColor(0xFFFF4444.toInt())
        } else {
            status.text = "⏸️ DETENIDO\nToca para activar"
            btn.text = "ACTIVAR PROTECCIÓN 🛡️"
            btn.setBackgroundColor(0xFF44AA44.toInt())
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
}
