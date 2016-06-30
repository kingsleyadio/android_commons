@file:JvmName("CommonUtils")

package ng.kingsley.android.extensions

/**
 * @author ADIO Kingsley O.
 * @since 22 May, 2016
 */

@Deprecated(message = "", replaceWith = ReplaceWith("org.jetbrains.anko.attempt(f)"))
fun <R> attempt(f: () -> R): Pair<R?, Throwable?> {
    var result: R? = null
    var exception: Throwable? = null
    try {
        result = f()
    } catch(t: Throwable) {
        exception = t
    }
    return result to exception
}