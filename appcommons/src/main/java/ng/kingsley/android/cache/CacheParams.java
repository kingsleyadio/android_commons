package ng.kingsley.android.cache;

import java.io.File;

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */
class CacheParams {
    final File cacheDir;
    final int appVersion;
    final long maxSize;

    CacheParams(int appVersion, File cacheDir, long maxSize) {
        this.appVersion = appVersion;
        this.cacheDir = cacheDir;
        this.maxSize = maxSize;
    }
}
