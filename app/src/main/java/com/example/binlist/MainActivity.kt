package com.example.binlist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://lookup.binlist.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(BinApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binInput = findViewById<EditText>(R.id.binInput)
        val fetchButton = findViewById<Button>(R.id.fetchButton)
        val resultText = findViewById<TextView>(R.id.resultText)
        val historyButton = findViewById<Button>(R.id.historyButton)

        database = AppDatabase.getDatabase(this)

        fetchButton.setOnClickListener {
            val bin = binInput.text.toString()
            if (bin.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = api.getBinInfo(bin)
                        val binInfoEntity = BinInfoEntity(
                            bin = bin,
                            scheme = response.scheme,
                            type = response.type,
                            brand = response.brand,
                            country = response.country?.name,
                            latitude = response.country?.latitude,
                            longitude = response.country?.longitude,
                            bankName = response.bank.name,
                            bankUrl = response.bank.url,
                            bankPhone = response.bank.phone,
                            bankCity = response.bank.city
                        )
                        database.binDao().insertBinInfo(binInfoEntity)

                        runOnUiThread {
                            resultText.text = response.toString()
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

        historyButton.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }
}
