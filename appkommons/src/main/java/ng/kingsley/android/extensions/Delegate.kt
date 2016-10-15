package ng.kingsley.android.extensions

import android.app.Activity
import android.app.Fragment
import android.app.ProgressDialog
import ng.kingsley.android.appkommons.R
import android.support.v4.app.Fragment as SupportFragment

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2016
 */

fun Activity.progressDialog() = lazy {
    ProgressDialog(this).apply {
        isIndeterminate = true
        setCancelable(false)
        setMessage(getString(R.string.wait))
    }
}

fun Fragment.progressDialog() = lazy {
    ProgressDialog(activity).apply {
        isIndeterminate = true
        setCancelable(false)
        setMessage(getString(R.string.wait))
    }
}

fun SupportFragment.progressDialog() = lazy {
    ProgressDialog(activity).apply {
        isIndeterminate = true
        setCancelable(false)
        setMessage(getString(R.string.wait))
    }
}
