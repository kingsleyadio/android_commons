package ng.kingsley.android.widget

import android.app.TimePickerDialog
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
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ng.kingsley.android.appkommons.R
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

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
        layout.setEndIconDrawable(R.drawable.ic_event_note_black_24dp)
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
        val currentSelection = (date ?: Date()).time.localToUtc()
        MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .apply { minDate?.let { setStart(it.time.localToUtc()) } }
                    .apply { maxDate?.let { setEnd(it.time.localToUtc()) } }
                    .setOpenAt(currentSelection)
                    .build()
            )
            .setSelection(currentSelection)
            .build()
            .apply {
                addOnPositiveButtonClickListener { selection ->
                    val selectedDate = Date(selection.utcToLocal())
                    if (proceedToTime) startTimePicker(selectedDate)
                    else date = selectedDate.also { onDateChangeListener?.invoke(it) }
                }
            }
            .show(getActivityContext().supportFragmentManager, null)
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

        TimePickerDialog(
            getActivityContext(), listener,
            cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false
        ).show()
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

    private fun Long.localToUtc(): Long = this + TimeZone.getDefault().getOffset(this)

    private fun Long.utcToLocal(): Long = this - TimeZone.getDefault().getOffset(this)

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
