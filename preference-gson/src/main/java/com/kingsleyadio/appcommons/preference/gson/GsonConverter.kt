package com.kingsleyadio.appcommons.preference.gson

import com.google.gson.Gson
import com.kingsleyadio.appcommons.preference.Converter
import java.lang.reflect.Type

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */
class GsonConverter(private val gson: Gson) : Converter {

    override fun <T> toString(content: T, type: Type): String {
        return gson.toJson(content, type)
    }

    override fun <T> fromString(serialized: String, type: Type): T? {
        return gson.fromJson(serialized, type)
    }
}
