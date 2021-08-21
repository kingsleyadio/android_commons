package com.kingsleyadio.appcommons.datetime.time

import android.app.Dialog
import android.os.Bundle
import android.view.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kingsleyadio.appcommons.datetime.R
import com.kingsleyadio.appcommons.datetime.databinding.DialogTimeBinding

/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
class TimePickerDialogFragment : BottomSheetDialogFragment() {
    private var selectedHour = -1
    private var selectedMinute = -1
    private var onTimeSelectedListener: OnTimeSelectedListener? = null
    private var timePickerConfig: ((HourAndMinutePicker) -> Unit)? = null

    private lateinit var binding: DialogTimeBinding

    fun setOnTimeChooseListener(listener: OnTimeSelectedListener) {
        onTimeSelectedListener = listener
    }

    fun withTimePicker(action: (HourAndMinutePicker) -> Unit) {
        timePickerConfig = action
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogTimeBinding.inflate(inflater)
        binding.buttonAccept.setOnClickListener {
            dismiss()
            onTimeSelectedListener?.let { listener ->
                val datePicker = binding.timePicker
                listener.onTimeSelected(datePicker.hour, datePicker.minute)
            }
        }
        if (selectedHour > 0) {
            setSelectedTime()
        }
        timePickerConfig?.invoke(binding.timePicker)
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

    fun setSelectedTime(hour: Int, minute: Int) {
        selectedHour = hour
        selectedMinute = minute
        setSelectedTime()
    }

    private fun setSelectedTime() {
        binding.timePicker.setTime(selectedHour, selectedMinute, false)
    }

    fun interface OnTimeSelectedListener {
        fun onTimeSelected(hour: Int, minute: Int)
    }
}
