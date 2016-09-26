package ng.kingsley.android.extensions

import com.google.gson.reflect.TypeToken
import ng.kingsley.android.helper.PersistenceHelper

/**
 * @author ADIO Kingsley O.
 * @since 17 Sep, 2016
 */

inline fun <reified T : Any> PersistenceHelper.retrieve(domain: String?, key: String): T? {
    return retrieve(domain, key, object : TypeToken<T>() {}.type)
}

inline fun <reified T : Any> PersistenceHelper.retrieve(domain: String?, key: String, crossinline fallback: () -> T): T {
    return retrieve(domain, key) ?: fallback()
}
