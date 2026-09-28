package com.shield7l
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.statusText).text = "🛡️ SHIELD7L — SISTEMA DE SEGURIDAD v1.0\n👤 Titular: J.E.Y.M.7\n🔒 PROTEGIDO — SOLO TÚ"
    }
}
