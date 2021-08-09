package com.kingsleyadio.appcommons.datetime

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.os.Parcelable
import android.text.InputType
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.FragmentActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kingsleyadio.appcommons.datetime.date.DatePickerDialogFragment
import com.kingsleyadio.appcommons.datetime.time.TimePickerDialogFragment
import com.kingsleyadio.appcommons.datetime.util.*
import java.util.*

/**
 * @author ADIO Kingsley O.
 * @since 26 May, 2016
 */
class DatetimeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    var onDateChangeListener: ((Date) -> Unit)? = null

    var date: Date? = null
        set(value) {
            field = value
            updateText()
        }

    var displayMode: DisplayMode = DisplayMode.DATE
        set(value) {
            field = value
            updateText()
        }

    var minDate: Date? = null
    var maxDate: Date? = null

    init {
        isFocusableInTouchMode = false
        isClickable = true
        isLongClickable = false
        isCursorVisible = false
        inputType = InputType.TYPE_CLASS_TEXT
        isSingleLine = true

        context.withStyledAttributes(attrs, R.styleable.DatetimeView, defStyleAttr) {
            val modeIndex =
                getInt(R.styleable.DatetimeView_displayMode, 0) % DisplayMode.values().size
            displayMode = DisplayMode.values()[modeIndex]
        }

        setOnClickListener { pickDate() }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val layout = getTextInputLayout() ?: return

        layout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        layout.setEndIconDrawable(
            when (displayMode) {
                DisplayMode.DATE -> R.drawable.ic_event_note_black_24dp
                DisplayMode.TIME -> R.drawable.ic_baseline_access_time_24
            }
        )
    }

    private fun updateText() {
        if (date == null) text = null
        else setText(DateFormat.format(displayMode.format, date))
    }

    override fun getDefaultEditable(): Boolean {
        return false
    }

    private fun pickDate() = when (displayMode) {
        DisplayMode.DATE -> startDatePicker()
        DisplayMode.TIME -> startTimePicker()
    }

    private fun startDatePicker() {
        DatePickerDialogFragment()
            .apply {
                withDatePicker { picker ->
                    minDate?.let { picker.setMinDate(it.time) }
                    maxDate?.let { picker.setMaxDate(it.time) }
                    val calendar = Calendar.getInstance()
                    calendar.time = (date ?: Date())
                    picker.setDate(calendar.year, calendar.month + 1, calendar.dayOfMonth)
                }
                setOnDateChooseListener { year, month, day ->
                    val calendar = Calendar.getInstance()
                    calendar.year = year
                    calendar.month = month - 1
                    calendar.dayOfMonth = day
                    date = calendar.time
                }
            }
            .show(getActivityContext().supportFragmentManager, null)
    }

    private fun startTimePicker(initialDate: Date? = null) {
        val cal = Calendar.getInstance()
        if (initialDate != null) cal.time = initialDate
        else if (date != null) cal.time = date!!

        TimePickerDialogFragment()
            .apply {
                withTimePicker { picker ->
                    picker.setTime(cal.hourOfDay, cal.minute)
                }
                setOnTimeChooseListener { hour, minute ->
                    cal.apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    date = cal.time.also { onDateChangeListener?.invoke(it) }
                }
            }
            .show(getActivityContext().supportFragmentManager, null)
    }

    private fun getTextInputLayout(): TextInputLayout? {
        var parent = parent
        while (parent is View) {
            if (parent is TextInputLayout) {
                return parent
            }
            parent = parent.getParent()
        }
        return null
    }

    private fun getActivityContext(): FragmentActivity {
        var context = context
        while (context !is FragmentActivity) {
            if (context is ContextWrapper) {
                context = context.baseContext
            } else {
                error("FragmentActivity context not found!")
            }
        }
        return context
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val b = state as Bundle
        super.onRestoreInstanceState(b.getParcelable(KEY_SUPER))

        displayMode = DisplayMode.valueOf(b.getString(KEY_DISPLAY_MODE, DisplayMode.DATE.name))
        if (b.containsKey(KEY_DATE)) {
            date = Date(b.getLong(KEY_DATE, System.currentTimeMillis()))
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val b = Bundle()
        b.putParcelable(KEY_SUPER, super.onSaveInstanceState())
        b.putString(KEY_DISPLAY_MODE, displayMode.name)

        val dateLocal = date
        if (dateLocal != null) b.putLong(KEY_DATE, dateLocal.time)
        return b
    }

    enum class DisplayMode(val format: String) {
        DATE("MMM d, yyyy"),
        TIME("hh:mma")
    }

    companion object {

        private const val KEY_SUPER = "key_super"
        private const val KEY_DISPLAY_MODE = "key_display_mode"
        private const val KEY_DATE = "key_date"
    }
}
