package ng.kingsley.android.appcommonsdemo

import android.os.Bundle
import android.widget.ArrayAdapter
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


        spinner_view.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf("Mango", "Suya"))
    }
}
