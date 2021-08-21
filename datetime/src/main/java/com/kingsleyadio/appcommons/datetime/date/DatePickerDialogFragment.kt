package com.kingsleyadio.appcommons.datetime.date

import android.app.Dialog
import android.os.Bundle
import android.view.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kingsleyadio.appcommons.datetime.R
import com.kingsleyadio.appcommons.datetime.databinding.DialogDateBinding

/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
class DatePickerDialogFragment : BottomSheetDialogFragment() {
    private var selectedYear = -1
    private var selectedMonth = -1
    private var selectedDay = -1
    private var onDateSelectedListener: DatePicker.OnDateSelectedListener? = null
    private var pickerConfig: ((DatePicker) -> Unit)? = null

    private lateinit var binding: DialogDateBinding

    fun setOnDateChooseListener(listener: DatePicker.OnDateSelectedListener) {
        onDateSelectedListener = listener
    }

    fun withDatePicker(action: (DatePicker) -> Unit) {
        pickerConfig = action
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogDateBinding.inflate(inflater)
        binding.buttonAccept.setOnClickListener {
            dismiss()
            onDateSelectedListener?.let { listener ->
                val datePicker = binding.datePicker
                listener.onDateSelected(datePicker.year, datePicker.month, datePicker.day)
            }
        }
        if (selectedYear > 0) {
            setSelectedDate()
        }
        pickerConfig?.invoke(binding.datePicker)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.DatePickerBottomDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 设置Content前设定
        dialog.setContentView(R.layout.dialog_date)
        dialog.setCanceledOnTouchOutside(true) // 外部点击取消
        dialog.window?.let { window ->
            window.attributes.windowAnimations = R.style.DatePickerDialogAnim
            val lp = window.attributes
            lp.gravity = Gravity.BOTTOM // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT // 宽度持平
            lp.dimAmount = 0.35f
            window.attributes = lp
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        return dialog
    }

    fun setSelectedDate(year: Int, month: Int, day: Int) {
        selectedYear = year
        selectedMonth = month
        selectedDay = day
        setSelectedDate()
    }

    private fun setSelectedDate() {
        binding.datePicker.setDate(selectedYear, selectedMonth, selectedDay, false)
    }

    fun interface OnDateSelectedListener {
        fun onDateChoose(year: Int, month: Int, day: Int)
    }
}
