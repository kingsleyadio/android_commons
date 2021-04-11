package com.kingsleyadio.appcommons.preference

import android.content.SharedPreferences
import java.lang.reflect.Type

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */
class PreferenceStore internal constructor(
    private val pref: SharedPreferences,
    private val converter: Converter
) {

    fun getInt(key: String, defaultValue: Int): Int = pref.getInt(key, defaultValue)

    fun getLong(key: String, defaultValue: Long): Long = pref.getLong(key, defaultValue)

    fun getFloat(key: String, defaultValue: Float): Float = pref.getFloat(key, defaultValue)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean = pref.getBoolean(key, defaultValue)

    fun getString(key: String): String? = pref.getString(key, null)

    fun getString(key: String, defaultValue: String): String = getString(key) ?: defaultValue

    fun <T> get(key: String, type: Type): T? = getString(key)?.let { converter.fromString(it, type) }

    fun putInt(key: String, value: Int): Unit = edit { putInt(key, value) }

    fun putLong(key: String, value: Long): Unit = edit { putLong(key, value) }

    fun putFloat(key: String, value: Float): Unit = edit { putFloat(key, value) }

    fun putBoolean(key: String, value: Boolean): Unit = edit { putBoolean(key, value) }

    fun putString(key: String, content: String?): Unit = edit { putString(key, content) }

    fun <T : Any> put(key: String, content: T, type: Type): Unit = putString(key, converter.toString(content, type))

    fun containsKey(key: String): Boolean = pref.contains(key)

    fun remove(key: String): Unit = edit { remove(key) }

    fun clear(): Unit = edit { clear() }

    private fun edit(action: SharedPreferences.Editor.() -> Unit) = pref.edit().apply(action).apply()
}
