package com.shield7l

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
        
        val textView = TextView(this)
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        textView.textSize = 24f
        textView.setPadding(48, 96, 48, 48)
        textView.text = """
SHIELD7L
SISTEMA DE SEGURIDAD

Titular: J.E.Y.M.7
Versión: 1.0

PROTEGIDO — SOLO TÚ
        """.trimIndent()
        
        layout.addView(textView)
        setContentView(layout)
    }
}
