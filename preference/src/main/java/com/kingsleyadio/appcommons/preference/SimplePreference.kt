package com.kingsleyadio.appcommons.preference

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.kingsleyadio.appcommons.util.Digests

/**
 * @author ADIO Kingsley O.
 * @since 13 Aug, 2015
 */
class SimplePreference(private val context: Application, private val converter: Converter) {

    fun with(domain: String): PreferenceStore {
        val storeName = Digests.md5(context.packageName + domain.trim())
        val preference = context.getSharedPreferences(storeName, Context.MODE_PRIVATE)

        return PreferenceStore(preference, converter)
    }

    fun with(wrapped: SharedPreferences): PreferenceStore {
        return PreferenceStore(wrapped, converter)
    }
}
