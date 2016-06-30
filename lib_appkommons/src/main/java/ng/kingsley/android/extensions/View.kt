package ng.kingsley.android.extensions

import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import android.support.v4.app.Fragment as SupportFragment

/**
 * @author ADIO Kingsley O.
 * @since 26 May, 2016
 */

fun <T : ImageView> T.setTintedDrawable(@DrawableRes resId: Int, tint: Int) {
    if (resId <= 0) return setImageDrawable(null)
    val drawable = ResourcesCompat.getDrawable(resources, resId, null)?.mutate()?.apply {
        DrawableCompat.setTint(DrawableCompat.wrap(this), tint)
    }
    setImageDrawable(drawable)
}

@Suppress("NAME_SHADOWING")
fun ImageView.loadUrl(url: String, @DrawableRes placeholder: Int = 0, @DrawableRes errorHolder: Int = 0) {
    var url = url
    url = Uri.encode(url, "@#&=*+-_.,:!?()/~'%")

    val builder = Picasso.with(context).load(url).fit().centerCrop()
    if (placeholder > 0) builder.placeholder(ResourcesCompat.getDrawable(context.resources, placeholder, null))
    if (errorHolder > 0) builder.error(errorHolder)

    builder.into(this)
}

fun <T : TextView> T.setTintedCompoundDrawables(@ColorInt tint: Int,
  @DrawableRes left: Int = 0, @DrawableRes top: Int = 0, @DrawableRes right: Int = 0, @DrawableRes bottom: Int = 0) {

    fun tinted(@DrawableRes resId: Int): Drawable? {
        if (resId <= 0) return null
        return ResourcesCompat.getDrawable(resources, resId, null)?.mutate()?.apply {
            DrawableCompat.setTint(DrawableCompat.wrap(this), tint)
        }
    }

    setCompoundDrawablesWithIntrinsicBounds(tinted(left), tinted(top), tinted(right), tinted(bottom))
}

var TextView.textColor: Int
    get() = currentTextColor
    set(value) = setTextColor(value)