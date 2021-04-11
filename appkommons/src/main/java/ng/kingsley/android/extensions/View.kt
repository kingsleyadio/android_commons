package ng.kingsley.android.extensions

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.kingsleyadio.appcommons.util.drawable
import com.kingsleyadio.appcommons.util.requireSystemService
import com.kingsleyadio.appcommons.util.tintedDrawable
import com.squareup.picasso.Picasso

/**
 * @author ADIO Kingsley O.
 * @since 26 May, 2016
 */

fun View.hideSoftInput() {
    val iMan = context.requireSystemService<InputMethodManager>()
    iMan.hideSoftInputFromWindow(windowToken, 0)
}

fun ImageView.setTintedDrawable(@DrawableRes resId: Int, tint: Int) {
    if (resId == 0) setImageDrawable(null)
    else setImageDrawable(context.tintedDrawable(resId, tint))
}

fun ImageView.loadImage(uri: Uri, placeHolder: Int = 0, errorHolder: Int = 0) {
    Picasso.get().load(uri).apply {
        if (placeHolder > 0) placeholder(context.drawable(placeHolder)!!)
        if (errorHolder > 0) error(errorHolder)
    }
        .fit()
        .centerCrop()
        .into(this)
}

fun ImageView.loadImage(url: String, placeHolder: Int = 0, errorHolder: Int = 0) {
    loadImage(Uri.parse(url), placeHolder, errorHolder)
}

fun <T : TextView> T.setTintedCompoundDrawables(
    @ColorInt tint: Int,
    @DrawableRes left: Int = 0,
    @DrawableRes top: Int = 0,
    @DrawableRes right: Int = 0,
    @DrawableRes bottom: Int = 0
) {

    fun tinted(@DrawableRes resId: Int): Drawable? {
        return if (resId == 0) null else context.tintedDrawable(resId, tint)
    }

    setCompoundDrawablesWithIntrinsicBounds(
        tinted(left),
        tinted(top),
        tinted(right),
        tinted(bottom)
    )
}

var TextView.textColor: Int
    get() = currentTextColor
    set(value) = setTextColor(value)
