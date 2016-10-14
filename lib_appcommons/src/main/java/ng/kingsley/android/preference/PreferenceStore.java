package ng.kingsley.android.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import ng.kingsley.android.util.DigestUtils;

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

public class PreferenceStore {

    private SharedPreferences pref;
    private Gson gson;

    PreferenceStore(Context context, Gson gson, String domain) {
        this.gson = gson;
        pref = context.getSharedPreferences(name(context, domain), Context.MODE_PRIVATE);
    }

    PreferenceStore(SharedPreferences wrapped, Gson gson) {
        this.gson = gson;
        this.pref = wrapped;
    }

    private static String name(Context context, String domain) {
        domain = (domain == null) ? "" : domain.trim();
        return DigestUtils.md5(context.getPackageName() + domain);
    }

    private SharedPreferences.Editor editor() {
        return pref.edit();
    }

    public void putInt(String key, int value) {
        editor().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        editor().putLong(key, value).apply();
    }

    public void putFloat(String key, float value) {
        editor().putFloat(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        editor().putBoolean(key, value).apply();
    }

    public void putString(String key, String content) {
        editor().putString(key, content).apply();
    }

    public <T> void put(String key, T content, Converter<T> converter) {
        putString(key, converter.toString(content));
    }

    public <T> void put(String key, T content, Class<T> clas) {
        put(key, content, new DefaultConverters.GsonConverter<>(clas, gson));
    }

    public <T> void put(String key, T content, TypeToken<T> token) {
        put(key, content, new DefaultConverters.GsonConverter<>(token, gson));
    }

    public int getInt(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return pref.getLong(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return pref.getFloat(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return pref.getBoolean(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public <T> T get(String key, Converter<T> converter) {
        return converter.fromString(getString(key, null));
    }

    public <T> T get(String key, Class<T> clas) {
        return get(key, new DefaultConverters.GsonConverter<>(clas, gson));
    }

    public <T> T get(String key, TypeToken<T> token) {
        return get(key, new DefaultConverters.GsonConverter<>(token, gson));
    }

    public void remove(String key) {
        editor().remove(key).apply();
    }

    public void clear() throws IOException {
        editor().clear().apply();
    }
}
