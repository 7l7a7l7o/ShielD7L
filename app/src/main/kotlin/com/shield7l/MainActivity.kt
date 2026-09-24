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
        val lay = LinearLayout(this)
        lay.orientation = LinearLayout.VERTICAL
        lay.setBackgroundColor(0xFF000000.toInt())
        lay.setPadding(48, 96, 48, 48)

        val title = TextView(this)
        title.setTextColor(0xFFFFFFFF.toInt())
        title.textSize = 28f
        title.text = """
SHIELD7L
SISTEMA DE SEGURIDAD

Titular: J.E.Y.M.7
Versión: 1.1
        """.trimIndent()
        lay.addView(title)

        status = TextView(this)
        status.setTextColor(0xFFCCCCCC.toInt())
        status.textSize = 18f
        status.setPadding(0, 32, 0, 32)
        lay.addView(status)

        btn = Button(this)
        btn.setOnClickListener { toggle() }
        lay.addView(btn)

        setContentView(lay)
        updateUI()
    }

    private fun toggle() {
        val i = Intent(this, SecurityMonitorService::class.java)
        if (SecurityMonitorService.isRunning) stopService(i)
        else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) startForegroundService(i)
        else startService(i)
        updateUI()
    }

    private fun updateUI() {
        if (SecurityMonitorService.isRunning) {
            status.text = "✅ PROTECCIÓN ACTIVA\nEscaneo de conexiones cada 3s"
            btn.text = "DETENER PROTECCIÓN"
            btn.setBackgroundColor(0xFFFF4444.toInt())
        } else {
            status.text = "⏸️ DETENIDO\nToca para activar el monitoreo"
            btn.text = "ACTIVAR PROTECCIÓN 🛡️"
            btn.setBackgroundColor(0xFF44AA44.toInt())
        }
    }

    override fun onResume() { super.onResume(); updateUI() }
}
