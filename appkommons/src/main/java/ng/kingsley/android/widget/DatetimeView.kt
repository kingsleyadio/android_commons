package ng.kingsley.android.widget

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.res.ResourcesCompat
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import ng.kingsley.android.appkommons.R
import ng.kingsley.android.extensions.setTintedCompoundDrawables
import ng.kingsley.android.util.Inputs
import java.util.Date

/**
 * @author ADIO Kingsley O.
 * @since 26 May, 2016
 */
class DatetimeView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  FrameLayout(context, attrs, defStyleAttr) {
    private val dateView: TextView

    var date: Date? = Date()
        get() {
            if (Inputs.hasBlank(dateView.text)) return null
            return field
        }
        set(value) {
            if (value == null) dateView.text = null
            else {
                field = value
                dateView.text = DateFormat.format(displayMode.format, value)
            }
        }
    var displayMode: DisplayMode = DisplayMode.DATE_ONLY
        set(value) {
            field = value
            if (date != null) dateView.text = DateFormat.format(field.format, date)
        }
    var hint: CharSequence
        get() = dateView.hint
        set(value) {
            dateView.hint = value
        }

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.view_datetime, this, true)
        dateView = v.findViewById(R.id.datetime) as TextView
        isClickable = true

        val tint = ResourcesCompat.getColor(resources, R.color.primary, null)
        dateView.setTintedCompoundDrawables(tint, right = R.drawable.ic_event_note_black_24dp)

        with(context.obtainStyledAttributes(attrs, R.styleable.DatetimeView, defStyleAttr, 0)) {
            val modeIndex = getInt(R.styleable.DatetimeView_displayMode, 0) % DisplayMode.values().size
            displayMode = DisplayMode.values()[modeIndex]
            hint = getString(R.styleable.DatetimeView_hint)

            recycle()
        }

    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val b = state as Bundle
        super.onRestoreInstanceState(b.getParcelable("super"))

        displayMode = DisplayMode.valueOf(b.getString("displayMode", DisplayMode.DATE_ONLY.name))
        if (b.containsKey("date")) {
            date = Date(b.getLong("date", System.currentTimeMillis()))
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val b = Bundle()
        b.putParcelable("super", super.onSaveInstanceState())
        b.putString("displayMode", displayMode.name)

        val dateLocal = date
        if (dateLocal != null) b.putLong("date", dateLocal.time)
        return b
    }

    enum class DisplayMode(val format: String) {
        DATE_ONLY("MMM d, yyyy"),
        TIME_ONLY("hh:mma"),
        DATE_TIME("MMM d, yyyy - hh:mma")
    }

}
