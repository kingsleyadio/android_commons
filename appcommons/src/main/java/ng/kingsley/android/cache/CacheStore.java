package ng.kingsley.android.cache;

import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ng.kingsley.android.util.DigestUtils;
import ng.kingsley.android.util.Log;

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */
public class CacheStore {
    private static final String TAG = CacheStore.class.getSimpleName();

    private static final int VALUE_COUNT = 1;
    private static final int VALUE_INDEX = 0;
    private final CacheParams params;
    private DiskLruCache cache;
    private Gson gson;

    CacheStore(CacheParams params, Gson gson) {
        this.params = params;
        this.gson = gson;

        try {
            cache = DiskLruCache.open(params.cacheDir, params.appVersion, VALUE_COUNT, params.maxSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String cacheKey(String plainKey) {
        return DigestUtils.md5(plainKey).toLowerCase();
    }

    public <T> boolean put(String key, T content, Converter<T> converter) {
        String cacheKey = cacheKey(key);

        try {
            DiskLruCache.Editor editor = cache.edit(cacheKey);
            if (editor != null) {
                OutputStream os = editor.newOutputStream(VALUE_INDEX);
                try {
                    converter.writeToStream(os, content);
                    editor.commit();
                    return true;
                } finally {
                    //noinspection ThrowFromFinallyBlock
                    os.close();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to put data. key=" + key, e);
        }

        return false;
    }

    public boolean putString(String key, String content) {
        return put(key, content, new DefaultConverters.StringConverter());
    }

    public <T> boolean put(String key, T content, Class<T> clas) {
        return put(key, content, new DefaultConverters.GsonConverter<>(clas, gson));
    }

    public <T> boolean put(String key, T content, TypeToken<T> token) {
        return put(key, content, new DefaultConverters.GsonConverter<>(token, gson));
    }

    public boolean putInputStream(String key, InputStream content) {
        return put(key, content, new DefaultConverters.InputStreamConverter());
    }

    public boolean putByteArray(String key, byte[] content) {
        return put(key, content, new DefaultConverters.ByteArrayConverter());
    }

    public <T> T get(String key, Converter<T> converter) {
        String cacheKey = cacheKey(key);

        try {
            DiskLruCache.Snapshot snapshot = cache.get(cacheKey);
            if (snapshot != null) {
                try {
                    InputStream is = snapshot.getInputStream(VALUE_INDEX);
                    return converter.readFromStream(is);
                } finally {
                    snapshot.close();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to get data. key=" + key, e);
        }

        return null;
    }

    public String getString(String key) {
        return get(key, new DefaultConverters.StringConverter());
    }

    public <T> T get(String key, Class<T> clas) {
        return get(key, new DefaultConverters.GsonConverter<>(clas, gson));
    }

    public <T> T get(String key, TypeToken<T> token) {
        return get(key, new DefaultConverters.GsonConverter<>(token, gson));
    }

    public InputStream getInputStream(String key) {
        return get(key, new DefaultConverters.InputStreamConverter());
    }

    public byte[] getByteArray(String key) {
        return get(key, new DefaultConverters.ByteArrayConverter());
    }

    public boolean containsKey(String key) {
        String cacheKey = cacheKey(key);
        try {
            return cache.get(cacheKey) != null;
        } catch (IOException e) {
            Log.e(TAG, "Failed to peek data. key=" + key, e);
        }
        return false;
    }

    public boolean remove(String key) {
        String cacheKey = cacheKey(key);

        try {
            return cache.remove(cacheKey);
        } catch (IOException e) {
            Log.e(TAG, "Failed to remove data. key=" + key, e);
            return false;
        }
    }

    public void clear() throws IOException {
        cache.delete();
        cache = DiskLruCache.open(params.cacheDir, params.appVersion, VALUE_COUNT, params.maxSize);
    }

    @VisibleForTesting
    DiskLruCache internalCache() {
        return cache;
    }

}
