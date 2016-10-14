package ng.kingsley.android.cache;

import android.app.Application;
import android.content.Context;
import android.os.StatFs;

import com.google.gson.Gson;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author ADIO Kingsley O.
 * @since 22 Mar, 2016
 */

@Singleton
public class SimpleCache {

    private static final String CACHE_DIR = "commons_cache";
    private static final String STORE_DIR = "commons_store";
    private static final int APP_VERSION = 1;
    private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    private final Context mContext;
    private final Gson mGson;
    private FileStore mCache;
    private FileStore mStore;

    @Inject
    SimpleCache(Application context, Gson gson) {
        mContext = context;
        mGson = gson;
    }

    public FileStore cache() {
        if (mCache == null) {
            synchronized (this) {
                if (mCache == null) {
                    File dir = new File(mContext.getCacheDir(), CACHE_DIR);
                    long size = calculateDiskCacheSize(dir);
                    CacheParams params = new CacheParams(APP_VERSION, dir, size);
                    mCache = new FileStore(params, mGson);
                }
            }
        }
        return mCache;
    }

    public FileStore store() {
        if (mStore == null) {
            synchronized (this) {
                if (mStore == null) {
                    File dir = new File(mContext.getFilesDir(), STORE_DIR);
                    long size = calculateDiskCacheSize(dir);
                    CacheParams params = new CacheParams(APP_VERSION, dir, size);
                    mStore = new FileStore(params, mGson);
                }
            }
        }
        return mStore;
    }


    private static long calculateDiskCacheSize(File dir) {
        long size = MIN_DISK_CACHE_SIZE;

        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
            // Target 2% of the total space.
            size = available / 50;
        } catch (IllegalArgumentException ignored) {
        }

        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
    }

}
