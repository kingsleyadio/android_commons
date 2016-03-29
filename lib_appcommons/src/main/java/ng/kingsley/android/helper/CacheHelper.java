package ng.kingsley.android.helper;

import android.app.Application;
import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.SimpleFuture;
import com.koushikdutta.async.util.FileCache;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Singleton;

import ng.kingsley.android.api.FutureCallback;
import ng.kingsley.android.util.Log;

/**
 * @author ADIO Kingsley O.
 * @since 22 Mar, 2016
 */

@Singleton
public class CacheHelper {

    private final Context mContext;

    @Inject
    CacheHelper(Application context) {
        mContext = context;
    }

    public <T> Future<T> put(String key, T content, FutureCallback<T> callback) {
        return Ion.getDefault(mContext)
          .cache(key)
          .put(content, new TypeToken<T>() {
          })
          .setCallback(callback);
    }

    public <T> Future<T> get(String key, FutureCallback<T> callback, Class<T> clas) {
        return Ion.getDefault(mContext)
          .cache(key)
          .as(clas)
          .setCallback(callback);
    }

    public <T> Future<T> get(String key, FutureCallback<T> callback, TypeToken<T> token) {
        return Ion.getDefault(mContext)
          .cache(key)
          .as(token)
          .setCallback(callback);
    }

    public Future<File> writeStream(final String key, final InputStream is, FutureCallback<File> callback) {
        final SimpleFuture<File> ret = new SimpleFuture<>();
        Ion.getIoExecutorService()
          .execute(new Runnable() {

              private String makeCacheKey() {
                  return key.replace(":", "_");
              }

              @Override
              public void run() {
                  FileCache ionCache = Ion.getDefault(mContext).getCache();
                  File tmp = ionCache.getTempFile();
                  try {
                      FileOutputStream os = new FileOutputStream(tmp);
                      byte[] bytes = new byte[4096];
                      int l;
                      while ((l = is.read(bytes)) != -1) {
                          os.write(bytes, 0, l);
                      }
                      String cacheKey = makeCacheKey();
                      ionCache.commitTempFiles(cacheKey, tmp);
                      ret.setComplete(ionCache.getFile(cacheKey));
                  } catch (IOException e) {
                      Log.e("API", "Could not copy stream content to cache", e);
                      ret.setComplete(e);
                  }
              }
          });
        return ret.setCallback(callback);
    }

    public void remove(String key) {
        Ion.getDefault(mContext).cache(key).remove();
    }
}
