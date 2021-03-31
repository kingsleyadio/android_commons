package ng.kingsley.android.extensions

import android.app.Activity
import android.app.ProgressDialog
import androidx.fragment.app.Fragment
import ng.kingsley.android.appkommons.R

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2016
 */
@Deprecated("Modal ProgressDialog is deprecated")
fun Activity.progressDialog() = lazy {
    ProgressDialog(this).apply {
        isIndeterminate = true
        setCancelable(false)
        setMessage(getString(R.string.wait))
    }
}

@Deprecated("Modal ProgressDialog is deprecated")
fun Fragment.progressDialog() = lazy {
    ProgressDialog(activity).apply {
        isIndeterminate = true
        setCancelable(false)
        setMessage(getString(R.string.wait))
    }
}
