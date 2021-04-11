package com.kingsleyadio.appcommons.preference

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * @author ADIO Kingsley O.
 * @since 13 Aug, 2015
 */
class SimplePreference(private val context: Application, private val converter: Converter) {

    fun with(domain: String): PreferenceStore {
        val preference = context.getSharedPreferences(domain, Context.MODE_PRIVATE)
        return PreferenceStore(preference, converter)
    }

    fun with(wrapped: SharedPreferences): PreferenceStore {
        return PreferenceStore(wrapped, converter)
    }

//    private fun name(context: Context, domain: String?): String {
//        var domain = domain
//        domain = domain?.trim { it <= ' ' } ?: ""
//        return DigestUtils.md5(context.packageName + domain)
//    }
}
