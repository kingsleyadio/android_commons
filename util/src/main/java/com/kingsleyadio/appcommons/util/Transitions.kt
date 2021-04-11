package com.kingsleyadio.appcommons.util

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.transition.Transition
import android.view.View
import androidx.core.util.Pair

/**
 * Helper class for creating content transitions used with [android.app.ActivityOptions].
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
object Transitions {

    fun makeSafe(transition: Transition): Transition {
        transition.excludeTarget(android.R.id.statusBarBackground, true)
        transition.excludeTarget(android.R.id.navigationBarBackground, true)
        return transition
    }

    /**
     * Create the transition participants required during a activity transition while
     * avoiding glitches with the system UI.
     *
     * @param activity         The activity used as start for the transition.
     * @param includeStatusBar If false, the status bar will not be added as the transition
     * participant.
     * @return All transition participants.
     */
    fun createSafePairs(
        activity: Activity,
        includeStatusBar: Boolean,
        vararg otherParticipants: Pair<*, *>
    ): Array<Pair<*, *>> {
        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        val decor = activity.window.decorView
        var statusBar: View? = null
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground)
        }
        val navBar = decor.findViewById<View>(android.R.id.navigationBarBackground)

        // Create pair of transition participants.
        val participants: List<Pair<*, *>> = listOfNotNull(
            statusBar?.transitionPair(),
            navBar?.transitionPair(),
            *otherParticipants
        )

        return participants.toTypedArray()
    }

    private fun View.transitionPair() = Pair(this, transitionName)
}
