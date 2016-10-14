package ng.kingsley.android.extensions

import java.util.Random

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

private val random = Random()

fun generatePin(length: Int): String {
    return random.nextInt(Math.pow(10.0, length.toDouble()).toInt())
      .toString()
      .padStart(length, '0')
}
