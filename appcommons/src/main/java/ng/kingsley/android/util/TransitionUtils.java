/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ng.kingsley.android.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.transition.Transition;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

/**
 * Helper class for creating content transitions used with {@link android.app.ActivityOptions}.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TransitionUtils {

    public static Transition makeSafe(Transition transition) {
        transition.excludeTarget(android.R.id.statusBarBackground, true);
        transition.excludeTarget(android.R.id.navigationBarBackground, true);
        return transition;
    }

    /**
     * Create the transition participants required during a activity transition while
     * avoiding glitches with the system UI.
     *
     * @param activity         The activity used as start for the transition.
     * @param includeStatusBar If false, the status bar will not be added as the transition
     *                         participant.
     * @return All transition participants.
     */
    public static Pair[] createSafePairs(
            @NonNull Activity activity,
            boolean includeStatusBar, @Nullable Pair... otherParticipants) {
        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        // Create pair of transition participants.
        List<Pair> participants = new ArrayList<>(3);
        addNonNullViewToPairs(statusBar, participants);
        addNonNullViewToPairs(navBar, participants);
        // only add transition participants if there's at least one none-null element
        if (otherParticipants != null && !(otherParticipants.length == 1
                                                   && otherParticipants[0] == null)) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        return participants.toArray(new Pair[0]);
    }

    private static void addNonNullViewToPairs(View view, List<Pair> participants) {
        if (view == null) {
            return;
        }
        participants.add(new Pair<>(view, view.getTransitionName()));
    }

}
