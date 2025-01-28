package com.example.binlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://lookup.binlist.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(BinApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val binInput = findViewById<EditText>(R.id.binInput)
        val fetchButton = findViewById<Button>(R.id.fetchButton)
        val resultText = findViewById<TextView>(R.id.resultText)

        fetchButton.setOnClickListener {
            val bin = binInput.text.toString()
            if (bin.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = api.getBinInfo(bin)
                        runOnUiThread {
                            resultText.text = buildString {
                                append("Cтрана: ${response.country?.name},")
                                append("  координаты: ${response.country?.longitude}-${response.country?.latitude}, ")
                                append(" тип карты: ${response.scheme}, данные банка(url: ${response.bank.url}, ")
                                append(" телефон: ${response.bank.phone}, город: ${response.bank.city})")
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            resultText.text = "Ошибка: ${e.message}"
                        }
                    }
                }
            } else {
                resultText.text = "Введите BIN"
            }
        }

    }
}