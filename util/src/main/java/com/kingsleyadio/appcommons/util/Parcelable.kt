package ng.kingsley.android.extensions

import android.os.Parcel
import android.os.Parcelable

/**
 * @author ADIO Kingsley O.
 * @since 26 Mar, 2017
 */

inline fun <reified T : Any> creator(crossinline factory: (Parcel) -> T) = object : Parcelable.Creator<T> {

    override fun createFromParcel(source: Parcel) = factory(source)

    override fun newArray(size: Int) = arrayOfNulls<T>(size)

}
