package ng.kingsley.android.widget

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatEditText
import android.text.InputType
import android.text.format.DateFormat
import android.util.AttributeSet
import ng.kingsley.android.appkommons.R
import ng.kingsley.android.extensions.color
import ng.kingsley.android.extensions.drawable
import java.util.Calendar
import java.util.Date

/**
 * @author ADIO Kingsley O.
 * @since 26 May, 2016
 */
class DatetimeView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var onDateChangeListener: ((Date) -> Unit)? = null

    var date: Date? = null
        set(value) {
            field = value
            updateText()
        }

    var displayMode: DisplayMode = DisplayMode.DATE_ONLY
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
        setSingleLine(true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DatetimeView, defStyleAttr, 0)

        try {
            val modeIndex = typedArray.getInt(R.styleable.DatetimeView_displayMode, 0) % DisplayMode.values().size
            displayMode = DisplayMode.values()[modeIndex]

            var drawableTint = ViewCompat.getBackgroundTintList(this)
                    ?: ColorStateList.valueOf(context.color(R.color.accent))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (compoundDrawableTintList != null) {
                    drawableTint = compoundDrawableTintList
                }
            }
            val rightDrawable = context.drawable(R.drawable.ic_event_note_black_24dp)!!
            DrawableCompat.setTintList(rightDrawable, drawableTint)

            compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.margin_widget)
            setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null)
        } finally {
            typedArray.recycle()
        }

        setOnClickListener { pickDate() }
    }

    private fun updateText() {
        if (date == null) text = null
        else setText(DateFormat.format(displayMode.format, date))
    }

    override fun getDefaultEditable(): Boolean {
        return false
    }

    private fun pickDate() = when (displayMode) {
        DisplayMode.DATE_ONLY -> startDatePicker(false)
        DisplayMode.DATE_TIME -> startDatePicker(true)
        DisplayMode.TIME_ONLY -> startTimePicker()
    }

    private fun startDatePicker(proceedToTime: Boolean) {
        val cal = Calendar.getInstance()
        if (date != null) cal.time = date
        else {
            val minDate = minDate
            val maxDate = maxDate
            if (minDate != null) cal.time = minDate
            else if (maxDate != null) cal.time = maxDate
        }

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            cal.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }

            if (proceedToTime) startTimePicker(cal.time)
            else date = cal.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

            }.time.also { onDateChangeListener?.invoke(it) }
        }

        DatePickerDialog(context, listener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).apply {
            minDate?.let { datePicker.minDate = it.time }
            maxDate?.let { datePicker.maxDate = it.time }

        }.show()
    }

    private fun startTimePicker(initialDate: Date? = null) {
        val cal = Calendar.getInstance()
        if (initialDate != null) cal.time = initialDate
        else if (date != null) cal.time = date

        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            cal.apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            date = cal.time.also { onDateChangeListener?.invoke(it) }
        }

        TimePickerDialog(context, listener,
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false
        ).show()
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val b = state as Bundle
        super.onRestoreInstanceState(b.getParcelable(KEY_SUPER))

        displayMode = DisplayMode.valueOf(b.getString(KEY_DISPLAY_MODE, DisplayMode.DATE_ONLY.name))
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
        DATE_ONLY("MMM d, yyyy"),
        TIME_ONLY("hh:mma"),
        DATE_TIME("MMM d, yyyy - hh:mma")
    }

    companion object {

        private const val KEY_SUPER = "key_super"
        private const val KEY_DISPLAY_MODE = "key_display_mode"
        private const val KEY_DATE = "key_date"
    }
}
