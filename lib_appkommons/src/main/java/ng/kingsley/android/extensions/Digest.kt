@file:JvmName("DigestUtils")

package ng.kingsley.android.extensions

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

/**
 * @author ADIO Kingsley O.
 * *
 * @since 22 Apr, 2015
 */

private val mRandom = SecureRandom()

@JvmName("md5")
fun String?.md5String(): String? {
    return this?.let {
        hexBytesToString(DigestUtils.md5(this))
    }
}

@JvmName("sha1")
fun String?.sha1String(): String? {
    return this?.let {
        hexBytesToString(DigestUtils.sha1(this))
    }
}

@JvmName("sha512")
fun String?.sha512String(): String? {
    return this?.let {
        hexBytesToString(DigestUtils.sha512(this))
    }
}

private fun hexBytesToString(bytes: ByteArray): String {
    return String(Hex.encodeHex(bytes))
}

fun generatePin(length: Int): String {
    return mRandom.nextInt(Math.pow(10.0, length.toDouble()).toInt()).toString().padStart(length, '0')
}