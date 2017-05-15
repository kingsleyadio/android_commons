package ng.kingsley.android.extensions

import com.google.gson.reflect.TypeToken
import ng.kingsley.android.preference.PreferenceStore

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

inline fun <reified T : Any> PreferenceStore.get(key: String): T {
    return get(key, object : TypeToken<T>() {})
}

inline fun <reified T : Any> PreferenceStore.getOrNull(key: String): T? {
    return get(key, object : TypeToken<T>() {})
}

inline fun <reified T : Any> PreferenceStore.put(key: String, value: T) {
    put(key, value, T::class.java)
}
