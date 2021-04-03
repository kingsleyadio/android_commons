package ng.kingsley.android.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import ng.kingsley.android.appkommons.R
import ng.kingsley.android.extensions.textColor

/**
 * @author ADIO Kingsley O.
 * @since 26 May, 2016
 */
class HeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardView(context, attrs) {

    private val container: ViewGroup
    private val headerView: View
    private val dividerView: View
    private val titleView: TextView

    var headerTitle: CharSequence
        get() = titleView.text
        set(value) {
            titleView.text = value
            headerView.isVisible = titleView.text.isNotBlank()
        }

    var headerColor: Int
        get() = titleView.textColor
        set(@ColorInt value) {
            titleView.textColor = value
            dividerView.setBackgroundColor(value)
        }

    var headerTextSize: Float
        get() = titleView.textSize
        set(value) = titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)

    var dividerThickness: Int
        get() = dividerView.height
        set(value) = dividerView.layoutParams.run { height = value }

    @Suppress("JoinDeclarationAndAssignment")
    private var isEditing: Boolean

    init {
        isEditing = true
        val v = LayoutInflater.from(context).inflate(R.layout.view_headerview, this, true)

        container = v.findViewById(R.id.content)
        headerView = v.findViewById(R.id.header)
        dividerView = v.findViewById(R.id.divider)
        titleView = headerView.findViewById(R.id.title)

        context.withStyledAttributes(
            attrs,
            R.styleable.HeaderView,
            defStyleRes = R.style.HeaderView
        ) {
            headerTitle = getString(R.styleable.HeaderView_headerTitle) ?: ""
            headerColor = getColor(R.styleable.HeaderView_headerColor, 0)
            headerTextSize = getDimension(R.styleable.HeaderView_headerTextSize, 0F)
            dividerThickness = getDimensionPixelSize(R.styleable.HeaderView_dividerThickness, 0)
        }

        isEditing = false
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams?) {
        if (isEditing) return super.addView(child, index, params)

        container.addView(child, index, params)
    }
}
