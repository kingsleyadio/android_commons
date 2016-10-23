package ng.kingsley.android.widget

import android.content.Context
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ng.kingsley.android.appkommons.R

/**
 * @author ADIO Kingsley O.
 * @since 26 May, 2016
 */
class HeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  CardView(context, attrs, defStyleAttr) {
    private val container: ViewGroup
    private val headerView: View
    private val titleView: TextView

    var headerTitle: CharSequence
        get() = titleView.text
        set(value) {
            titleView.text = value
            headerView.visibility = if (TextUtils.isEmpty(titleView.text)) View.GONE else View.VISIBLE
        }

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.view_headerview, this, false)
        super.addView(v, -1, v.layoutParams)

        container = v.findViewById(R.id.content) as ViewGroup
        headerView = v.findViewById(R.id.header)
        titleView = headerView.findViewById(R.id.title) as TextView

        val ta = context.obtainStyledAttributes(attrs, R.styleable.HeaderView, defStyleAttr, 0)
        headerTitle = ta.getString(R.styleable.HeaderView_headerTitle) ?: ""

        ta.recycle()
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        container.addView(child, index, params)
    }

    override fun removeView(view: View?) {
        container.removeView(view)
    }

    override fun removeViewAt(index: Int) {
        container.removeViewAt(index)
    }

    override fun removeAllViews() {
        container.removeAllViews()
    }
}
