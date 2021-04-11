package com.kingsleyadio.appcommons.preference.moshi

import com.kingsleyadio.appcommons.preference.Converter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

/**
 * @author Kingsley Adio
 * @since 11 Apr, 2021
 */
class MoshiConverter(private val moshi: Moshi) : Converter {

    override fun <T> toString(content: T, type: Type): String {
        return moshi.adapter<T>(type).toJson(content)
    }

    override fun <T> fromString(serialized: String, type: Type): T? {
        return moshi.adapter<T>(type).fromJson(serialized)
    }
}
