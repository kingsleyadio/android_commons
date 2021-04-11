package com.kingsleyadio.appcommons.util

import android.os.Bundle

/**
 * @author ADIO Kingsley O.
 * @since 25 Mar, 2017
 */

fun Map<String, String>.toStringBundle() = Bundle(size).also {
    forEach { (k, v) -> it.putString(k, v) }
}

fun Bundle.toStringMap() = HashMap<String, String>(size()).also {
    keySet().forEach { k -> it[k] = getString(k)!! }
}

fun Bundle.toMap() = HashMap<String, Any>(size()).also {
    keySet().forEach { k -> it[k] = get(k)!! }
}
