package com.kingsleyadio.appcommons.preference

import java.lang.reflect.Type

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */
interface Converter {

    fun <T> toString(content: T, type: Type): String

    fun <T> fromString(serialized: String, type: Type): T?
}
