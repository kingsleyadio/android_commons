package ng.kingsley.android.extensions

import android.content.ContentResolver
import android.net.Uri
import java.io.FileNotFoundException

/**
 * @author ADIO Kingsley O.
 * @since 30 Apr, 2017
 */

fun ContentResolver.uriExists(uri: Uri): Boolean {
    return try {
        val afd = openAssetFileDescriptor(uri, "r")?.also { it.close() }
        afd != null
    } catch (e: FileNotFoundException) {
        false
    }
}
