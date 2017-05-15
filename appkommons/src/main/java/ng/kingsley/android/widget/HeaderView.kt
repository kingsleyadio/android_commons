package ng.kingsley.android.widget

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ng.kingsley.android.appkommons.R
import ng.kingsley.android.extensions.findView
import ng.kingsley.android.extensions.isVisible
import ng.kingsley.android.extensions.textColor

/**
 * @author ADIO Kingsley O.
 * @since 26 May, 2016
 */
class HeaderView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
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
        set(value) = dividerView.layoutParams.run {
            height = value
        }

    @Suppress("JoinDeclarationAndAssignment")
    private var isEditing: Boolean

    init {
        isEditing = true
        val v = LayoutInflater.from(context).inflate(R.layout.view_headerview, this, true)

        container = v.findView(R.id.content)
        headerView = v.findView(R.id.header)
        dividerView = v.findView(R.id.divider)
        titleView = headerView.findView(R.id.title)

        val values = context.obtainStyledAttributes(attrs, R.styleable.HeaderView, defStyleAttr, R.style.HeaderView)
        try {
            headerTitle = values.getString(R.styleable.HeaderView_headerTitle) ?: ""
            headerColor = values.getColor(R.styleable.HeaderView_headerColor, 0)
            headerTextSize = values.getDimension(R.styleable.HeaderView_headerTextSize, 0F)
            dividerThickness = values.getDimensionPixelSize(R.styleable.HeaderView_dividerThickness, 0)
        } finally {
            values.recycle()
        }

        isEditing = false
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams?) {
        if (isEditing) return super.addView(child, index, params)
        container.addView(child, index, params)
    }
}
