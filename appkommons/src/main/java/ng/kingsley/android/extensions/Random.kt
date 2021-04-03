package ng.kingsley.android.extensions

import kotlin.math.pow
import kotlin.random.Random

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

fun generatePin(length: Int): String {
    return Random.nextInt(10.0.pow(length).toInt())
      .toString()
      .padStart(length, '0')
}
