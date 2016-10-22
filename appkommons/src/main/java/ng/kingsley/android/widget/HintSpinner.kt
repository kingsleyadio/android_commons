package ng.kingsley.android.widget

import android.content.Context
import android.database.DataSetObserver
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatSpinner
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import ng.kingsley.android.appkommons.R

/**
 * @author ADIO Kingsley O.
 * @since 22 Oct, 2016
 */
class HintSpinner @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  AppCompatSpinner(context, attrs, defStyleAttr) {

    private var mAdapter: InternalAdapter? = null

    var hint: CharSequence

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.HintSpinner, defStyleAttr, R.style.HintSpinner)
        hint = ta.getString(R.styleable.HintSpinner_android_hint) ?: ""
        ta.recycle()
    }

    override fun performClick(): Boolean {
        if (mAdapter != null) {
            AlertDialog.Builder(context)
              .setTitle(hint)
              .setAdapter(mAdapter) {
                  d, w ->
                  setSelection(w + 1)
              }.show()
        }

        // Eat click event
        return true
    }

    override fun performLongClick(): Boolean {
        // Eat long click event
        return true
    }

    override fun setAdapter(adapter: SpinnerAdapter?) {
        if (adapter == null) {
            mAdapter = null
            return super.setAdapter(null)
        }

        val internalAdapter = InternalAdapter(adapter)
        mAdapter = internalAdapter
        super.setAdapter(HintSpinnerAdapter(internalAdapter, hint))
    }

    class InternalAdapter(val adapter: SpinnerAdapter) : ListAdapter, SpinnerAdapter by adapter {

        override fun areAllItemsEnabled(): Boolean {
            return if (adapter is ListAdapter) adapter.areAllItemsEnabled() else true
        }

        override fun isEnabled(position: Int): Boolean {
            return if (adapter is ListAdapter) adapter.isEnabled(position) else true
        }

    }

    class HintSpinnerAdapter(private val mAdapter: SpinnerAdapter, private val hint: CharSequence) : BaseAdapter() {

        private var mHintView: View? = null

        private fun getHintView(parent: ViewGroup): View {
            var view = mHintView
            if (view == null) {
                view = LayoutInflater.from(parent.context)
                  .inflate(android.R.layout.simple_spinner_item, parent, false)
                mHintView = view
            }

            (view!!.findViewById(android.R.id.text1) as TextView).text = hint
            return view
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

        override fun getItemViewType(position: Int): Int {
            return 0
        }

        override fun getViewTypeCount(): Int {
            return 1
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

        override fun areAllItemsEnabled(): Boolean {
            return false
        }

        override fun isEnabled(position: Int): Boolean {
            return position != 0
        }

        companion object {

            private const val EXTRA = 1
        }

    }
}
