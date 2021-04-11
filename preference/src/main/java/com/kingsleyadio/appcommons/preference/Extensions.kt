package com.kingsleyadio.appcommons.preference

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

inline fun <reified T : Any> PreferenceStore.getValue(key: String): T {
    return get(key) ?: error("value for key '$key' is null")
}

inline fun <reified T : Any> PreferenceStore.get(key: String): T? {
    return get(key, javaTypeOf<T>())
}

inline fun <reified T : Any> PreferenceStore.put(key: String, content: T) {
    put(key, content, javaTypeOf<T>())
}
