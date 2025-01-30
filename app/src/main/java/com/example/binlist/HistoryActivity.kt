package com.example.binlist

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val historyTextView = findViewById<TextView>(R.id.historyTextView)
        database = AppDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            val historyList = database.binDao().getAllBinHistory()
            val historyText = historyList.joinToString("\n\n") { bin ->
                "BIN: ${bin.bin}\nБанк: ${bin.bankName}\nСтрана: ${bin.country}\n"
            }
            runOnUiThread {
                historyTextView.text = historyText.ifEmpty { "История пуста" }
            }
        }
    }
}
