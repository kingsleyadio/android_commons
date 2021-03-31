package ng.kingsley.android.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.database.DataSetObserver
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.menu.ShowableListMenu
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.ForwardingListener
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.ViewUtils
import androidx.core.content.withStyledAttributes
import androidx.core.view.ViewCompat
import ng.kingsley.android.appkommons.R
import kotlin.math.max
import kotlin.math.min

/**
 * @author ADIO Kingsley O.
 * @since 22 Oct, 2016
 */
@SuppressLint("RestrictedApi")
@Deprecated("Consider migrating to a non-spinner based solution")
class HintSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatSpinner(context, attrs, defStyleAttr),
    DialogInterface.OnClickListener {

    private val mPopup: ListPopupWindow

    /** Temporary holder for setAdapter() calls from the super constructor.  */
    private var mTempAdapter: SpinnerAdapter? = null

    private val mPopupSet: Boolean

    /** Forwarding listener used to implement drag-to-open.  */
    private var mForwardingListener: ForwardingListener? = null

    private val MAX_ITEMS_MEASURED = 15

    internal var mDropDownWidth: Int = 0

    internal val mTempRect = Rect()

    var hint: CharSequence = ""
        set(value) {
            if (field == value) return

            field = value
            val adapter = this@HintSpinner.adapter
            if (!mPopup.isShowing && adapter is BaseAdapter) {
                adapter.notifyDataSetChanged()
            }
        }

    private var hintLayoutRes: Int = R.layout.list_simpleitem_no_offset

    init {
        val popup = DropdownPopup(context, attrs, defStyleAttr)

        popupContext.withStyledAttributes(attrs, R.styleable.Spinner, defStyleAttr) {
            mDropDownWidth = getLayoutDimension(
                androidx.appcompat.R.styleable.Spinner_android_dropDownWidth,
                LayoutParams.WRAP_CONTENT
            )
            popup.setBackgroundDrawable(
                getDrawable(androidx.appcompat.R.styleable.Spinner_android_popupBackground)
            )
        }

        mPopup = popup
        mForwardingListener = object : ForwardingListener(this) {

            override fun getPopup(): ShowableListMenu {
                return mPopup
            }

            public override fun onForwardingStarted(): Boolean {
                if (!mPopup.isShowing) {
                    mPopup.show()
                }
                return true
            }
        }

        context.withStyledAttributes(attrs, R.styleable.HintSpinner, defStyleAttr) {
            hint = getString(R.styleable.HintSpinner_hint) ?: ""
            hintLayoutRes = getResourceId(
                R.styleable.HintSpinner_hint_layout,
                R.layout.list_simpleitem_no_offset
            )
        }

        mPopupSet = true

        // Base constructors can call setAdapter before we initialize mPopup.
        // Finish setting things up if this happened.
        if (mTempAdapter != null) {
            adapter = mTempAdapter
            mTempAdapter = null
        }
    }

    override fun setPopupBackgroundDrawable(background: Drawable?) {
        mPopup.setBackgroundDrawable(background)
    }

    override fun setPopupBackgroundResource(@DrawableRes resId: Int) {
        val drawable = AppCompatResources.getDrawable(popupContext, resId)
        setPopupBackgroundDrawable(drawable)
    }

    override fun getPopupBackground(): Drawable? {
        return mPopup.background
    }

    override fun setDropDownVerticalOffset(pixels: Int) {
        mPopup.verticalOffset = pixels
    }

    override fun getDropDownVerticalOffset(): Int {
        return mPopup.verticalOffset
    }

    override fun setDropDownHorizontalOffset(pixels: Int) {
        mPopup.horizontalOffset = pixels
    }

    /**
     * Get the configured horizontal offset in pixels for the spinner's popup window of choices.
     * Only valid in [.MODE_DROPDOWN]; other modes will return 0.

     * @return Horizontal offset in pixels
     */
    override fun getDropDownHorizontalOffset(): Int {
        return mPopup.horizontalOffset
    }

    override fun setDropDownWidth(pixels: Int) {
        mDropDownWidth = pixels
    }

    override fun getDropDownWidth(): Int {
        return mDropDownWidth
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val forwardingListener = mForwardingListener
        if (forwardingListener != null && forwardingListener.onTouch(this, event)) {
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            val measuredWidth = measuredWidth
            setMeasuredDimension(
                measuredWidth.coerceIn(
                    compatMeasureContentWidth(adapter, background),
                    MeasureSpec.getSize(widthMeasureSpec)
                ),
                measuredHeight
            )
        }
    }

    override fun performClick(): Boolean {
        if (!mPopup.isShowing) {
            mPopup.show()
        }

        // Eat event anyway
        return true
    }

    override fun setAdapter(adapter: SpinnerAdapter?) {
        if (!mPopupSet) {
            mTempAdapter = adapter
            return
        }
        if (adapter == null) {
            mPopup.setAdapter(null)
            super.setAdapter(null)
            return
        }

        super.setAdapter(HintSpinnerAdapter(adapter))
        mPopup.setAdapter(DropdownAdapter(adapter))
    }

    internal fun compatMeasureContentWidth(adapter: SpinnerAdapter?, background: Drawable?): Int {
        if (adapter == null) {
            return 0
        }

        var width = 0
        var itemView: View? = null
        var itemType = 0
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.UNSPECIFIED)

        // Make sure the number of items we'll measure is capped. If it's a huge data set
        // with wildly varying sizes, oh well.
        var start = max(0, selectedItemPosition)
        val end = min(adapter.count, start + MAX_ITEMS_MEASURED)
        val count = end - start
        start = max(0, start - (MAX_ITEMS_MEASURED - count))
        for (i in start until end) {
            val positionType = adapter.getItemViewType(i)
            if (positionType != itemType) {
                itemType = positionType
                itemView = null
            }
            itemView = adapter.getView(i, itemView, this)
            if (itemView!!.layoutParams == null) {
                itemView.layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
            }
            itemView.measure(widthMeasureSpec, heightMeasureSpec)
            width = width.coerceAtLeast(itemView.measuredWidth)
        }

        // Add background padding to measured width
        if (background != null) {
            background.getPadding(mTempRect)
            width += mTempRect.left + mTempRect.right
        }

        return width
    }

    override fun onDetachedFromWindow() {
        if (mPopup.isShowing) mPopup.dismiss()
        super.onDetachedFromWindow()
    }

    class DropdownAdapter(
        private val adapter: SpinnerAdapter
    ) : ListAdapter,
        SpinnerAdapter by adapter {

        override fun areAllItemsEnabled(): Boolean {
            return if (adapter is ListAdapter) adapter.areAllItemsEnabled() else true
        }

        override fun isEnabled(position: Int): Boolean {
            return if (adapter is ListAdapter) adapter.isEnabled(position) else true
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            return adapter.getDropDownView(position, convertView, parent)
        }

    }

    private inner class HintSpinnerAdapter(private val mAdapter: SpinnerAdapter) : BaseAdapter() {

        private val EXTRA = 1
        private var hintView: View? = null

        private fun getHintView(parent: ViewGroup): View {
            val view: View = hintView ?: LayoutInflater.from(parent.context)
                .inflate(this@HintSpinner.hintLayoutRes, parent, false)
                .apply { hintView = this@apply }

            with(view.findViewById(android.R.id.text1) as TextView) {
                text = this@HintSpinner.hint
                return this@with
            }
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            if (position == 0) return getHintView(parent)
            return mAdapter.getView(Math.max(0, position - EXTRA), null, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            throw IllegalStateException("This method should never get called!")
        }

        override fun getCount(): Int {
            val count = mAdapter.count
            return if (count == 0) 0 else count + EXTRA
        }

        override fun getItem(position: Int): Any? {
            return if (position == 0) null else mAdapter.getItem(position - EXTRA)
        }

        override fun getItemId(position: Int): Long {
            return if (position >= EXTRA) mAdapter.getItemId(position - EXTRA) else (position - EXTRA).toLong()
        }

        override fun hasStableIds(): Boolean {
            return mAdapter.hasStableIds()
        }

        override fun isEmpty(): Boolean {
            return mAdapter.isEmpty
        }

        override fun registerDataSetObserver(observer: DataSetObserver) {
            mAdapter.registerDataSetObserver(observer)
        }

        override fun unregisterDataSetObserver(observer: DataSetObserver) {
            mAdapter.unregisterDataSetObserver(observer)
        }

    }

    private inner class DropdownPopup(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : ListPopupWindow(context, attrs, defStyleAttr) {
        private var mAdapter: ListAdapter? = null
        private val mVisibleRect = Rect()

        init {
            val spinner = this@HintSpinner
            anchorView = spinner
            isModal = true
            promptPosition = POSITION_PROMPT_BELOW

            setOnItemClickListener { _, v, position, _ ->
                spinner.setSelection(position + 1)
                if (onItemClickListener != null) {
                    spinner.performItemClick(v, position, mAdapter!!.getItemId(position))
                }
                dismiss()
            }
        }

        override fun setAdapter(adapter: ListAdapter?) {
            super.setAdapter(adapter)
            mAdapter = adapter
        }

        internal fun computeContentWidth() {
            val background = background
            var hOffset = 0
            if (background != null) {
                background.getPadding(mTempRect)
                hOffset =
                    if (ViewUtils.isLayoutRtl(this@HintSpinner)) mTempRect.right else -mTempRect.left
            } else {
                mTempRect.left = 0
                mTempRect.right = 0
            }

            val spinnerPaddingLeft = this@HintSpinner.paddingLeft
            val spinnerPaddingRight = this@HintSpinner.paddingRight
            val spinnerWidth = this@HintSpinner.width
            if (mDropDownWidth == WRAP_CONTENT) {
                var contentWidth =
                    compatMeasureContentWidth(mAdapter as? SpinnerAdapter, getBackground())
                val contentWidthLimit =
                    context.resources.displayMetrics.widthPixels - mTempRect.left - mTempRect.right
                if (contentWidth > contentWidthLimit) {
                    contentWidth = contentWidthLimit
                }
                setContentWidth(
                    contentWidth.coerceAtLeast(spinnerWidth - spinnerPaddingLeft - spinnerPaddingRight)
                )
            } else if (mDropDownWidth == MATCH_PARENT) {
                setContentWidth(spinnerWidth - spinnerPaddingLeft - spinnerPaddingRight)
            } else {
                setContentWidth(mDropDownWidth)
            }
            hOffset += if (ViewUtils.isLayoutRtl(this@HintSpinner)) {
                spinnerWidth - spinnerPaddingRight - width
            } else {
                spinnerPaddingLeft
            }
            horizontalOffset = hOffset
        }

        override fun show() {
            val wasShowing = isShowing

            computeContentWidth()

            inputMethodMode = INPUT_METHOD_NOT_NEEDED
            super.show()
            val listView = listView ?: return
            listView.choiceMode = ListView.CHOICE_MODE_SINGLE
            val selection = max(this@HintSpinner.selectedItemPosition - 1, 0)
            setSelection(selection)

            if (wasShowing) {
                // Skip setting up the layout/dismiss listener below. If we were previously
                // showing it will still stick around.
                return
            }

            // Make sure we hide if our anchor goes away.
            // TODO: This might be appropriate to push all the way down to PopupWindow,
            // but it may have other side effects to investigate first. (Text editing handles, etc.)
            val vto = viewTreeObserver ?: return
            val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
                if (!isVisibleToUser(this@HintSpinner)) {
                    dismiss()
                } else {
                    computeContentWidth()

                    // Use super.show here to update; we don't want to move the selected
                    // position or adjust other things that would be reset otherwise.
                    super@DropdownPopup.show()
                }
            }
            vto.addOnGlobalLayoutListener(layoutListener)
            setOnDismissListener {
                vto.removeOnGlobalLayoutListener(layoutListener)
            }
        }

        /**
         * Simplified version of the the hidden View.isVisibleToUser()
         */
        internal fun isVisibleToUser(view: View): Boolean {
            return ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(mVisibleRect)
        }
    }
}
