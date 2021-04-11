package com.kingsleyadio.appcommons.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kingsleyadio.appcommons.demo.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle(R.string.app_name)

        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, get(Calendar.YEAR) - 16)
        }
        with(binding) {
            dateView.maxDate = cal.time
            dateView.onDateChangeListener = {
                Toast.makeText(this@MainActivity, "Date changed: $it", Toast.LENGTH_SHORT).show()
            }
            textDateView.isEnabled = false
        }
    }
}
