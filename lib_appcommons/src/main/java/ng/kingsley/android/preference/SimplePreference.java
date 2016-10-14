package ng.kingsley.android.preference;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author ADIO Kingsley O.
 * @since 13 Aug, 2015
 */
@Singleton
public class SimplePreference {

    private final Context mContext;
    private final Gson GSON;

    @Inject
    public SimplePreference(Application context, Gson gson) {
        mContext = context;
        GSON = gson;
    }

    public PreferenceStore with(String domain) {
        return new PreferenceStore(mContext, GSON, domain);
    }

    public PreferenceStore with(SharedPreferences wrapped) {
        return new PreferenceStore(wrapped, GSON);
    }
}
