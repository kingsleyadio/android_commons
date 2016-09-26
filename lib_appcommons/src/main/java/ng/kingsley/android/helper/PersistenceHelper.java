package ng.kingsley.android.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author ADIO Kingsley O.
 * @since 13 Aug, 2015
 */
@Singleton
public class PersistenceHelper {

    private final Context mContext;
    private final Gson GSON;

    @Inject
    public PersistenceHelper(Application context, Gson gson) {
        mContext = context;
        GSON = gson;
    }

    private static String getName(Context context, String domain) {
        if (TextUtils.isEmpty(domain)) {
            return context.getPackageName();
        }
        return context.getPackageName() + "_" + domain;
    }

    public <T> void persist(String domain, String key, T object) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(getName(mContext, domain),
          Context.MODE_PRIVATE).edit();
        String string = GSON.toJson(object, new TypeToken<T>() {
        }.getType());
        editor.putString(key, string);
        editor.apply();
    }

    public <T> void persist(String key, T object) {
        persist(null, key, object);
    }

    public <T> T retrieve(String domain, String key, Class<T> clas) {
        SharedPreferences pref = mContext.getSharedPreferences(getName(mContext, domain), Context.MODE_PRIVATE);
        String object = pref.getString(key, null);
        return GSON.fromJson(object, clas);
    }

    public <T> T retrieve(String key, Class<T> clas) {
        return retrieve(null, key, clas);
    }

    public <T> T retrieve(String domain, String key, Class<T> clas, T defaultValue) {
        T object = retrieve(domain, key, clas);
        return (object == null) ? defaultValue : object;
    }

    public <T> T retrieve(String key, Class<T> clas, T defaultValue) {
        return retrieve(null, key, clas, defaultValue);
    }

    public <T> T retrieve(String domain, String key, Type type) {
        SharedPreferences pref = mContext.getSharedPreferences(getName(mContext, domain), Context.MODE_PRIVATE);
        String object = pref.getString(key, null);
        return GSON.fromJson(object, type);
    }

    public <T> T retrieve(String key, Type type) {
        return retrieve(null, key, type);
    }

    public <T> T retrieve(String domain, String key, Type type, T defaultValue) {
        T object = retrieve(domain, key, type);
        return (object == null) ? defaultValue : object;
    }

    public <T> T retrieve(String key, Type type, T defaultValue) {
        return retrieve(null, key, type, defaultValue);
    }


    public void wipe(String domain, String key) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(getName(mContext, domain),
          Context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.apply();
    }

    public void wipe(String key) {
        wipe(null, key);
    }
}
