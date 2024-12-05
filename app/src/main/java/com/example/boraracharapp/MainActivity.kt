package com.example.boraracharapp

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tts: TextToSpeech
    private lateinit var editTextValor: EditText
    private lateinit var editTextPessoas: EditText
    private lateinit var textViewResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar componentes
        editTextValor = findViewById(R.id.editTextValor)
        editTextPessoas = findViewById(R.id.editTextPessoas)
        textViewResultado = findViewById(R.id.textViewResultado)
        val buttonShare: Button = findViewById(R.id.buttonShare)

        // Inicializar TTS
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.getDefault()
            }
        }

        // Adicionar listeners nos campos
        editTextValor.addTextChangedListener {
            calcularDivisao()
        }

        editTextPessoas.addTextChangedListener {
            calcularDivisao()
        }

        // Compartilhar resultado
        buttonShare.setOnClickListener {
            compartilharResultado()
        }
    }

    private fun calcularDivisao() {
        val valor = editTextValor.text.toString().toDoubleOrNull() ?: 0.0
        val pessoas = editTextPessoas.text.toString().toIntOrNull() ?: 1

        if (pessoas > 0) {
            val resultado = valor / pessoas
            val textoResultado = "Valor por pessoa: R$ %.2f".format(resultado)
            textViewResultado.text = textoResultado
            falarValor(textoResultado)
        }
    }

    private fun falarValor(texto: String) {
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun compartilharResultado() {
        val textoResultado = textViewResultado.text.toString()
        val intent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT, textoResultado)
            type = "text/plain"
        }
        startActivity(android.content.Intent.createChooser(intent, "Compartilhar via"))
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}
