package com.kingsleyadio.appcommons.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.text.Charsets.UTF_8

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */
object Digests {
    private const val HEX_CHARS = "0123456789ABCDEF"

    fun md5(input: String): String {
        val hex = getDigest("MD5").digest(input.toByteArray(UTF_8))
        return hex.hexBytesToString()
    }

    fun sha1(input: String): String {
        val hex = getDigest("SHA-1").digest(input.toByteArray(UTF_8))
        return hex.hexBytesToString()
    }

    fun sha256(input: String): String {
        val hex = getDigest("SHA-256").digest(input.toByteArray(UTF_8))
        return hex.hexBytesToString()
    }

    fun sha512(input: String): String {
        val hex = getDigest("SHA-512").digest(input.toByteArray(UTF_8))
        return hex.hexBytesToString()
    }

    private fun getDigest(algorithm: String): MessageDigest {
        return try {
            MessageDigest.getInstance(algorithm)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException(e)
        }
    }

    private fun ByteArray.hexBytesToString(): String {
        val hexChars = CharArray(size shl 1)
        for (j in indices) {
            val v = this[j].toInt() and 0xFF
            hexChars[j shl 1] = HEX_CHARS[v ushr 4]
            hexChars[(j shl 1) + 1] = HEX_CHARS[v and 0x0F]
        }
        return String(hexChars)
    }
}
