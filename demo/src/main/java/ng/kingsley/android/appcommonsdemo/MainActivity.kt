package ng.kingsley.android.appcommonsdemo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ng.kingsley.android.app.BaseActivity
import java.util.Calendar

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle(R.string.app_name)

        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, get(Calendar.YEAR) - 16)
        }
        date_view.maxDate = cal.time
        date_view.onDateChangeListener = { Toast.makeText(this, "Date changed: $it", Toast.LENGTH_SHORT).show() }


        text_date_view.isEnabled = false
        spinner_view.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf("Mango", "Suya"))
    }
}
