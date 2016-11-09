package ng.kingsley.android.cache;

import android.support.v4.util.LruCache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * @author ADIO Kingsley O.
 * @since 09 Nov, 2016
 */
class GsonConverter<T> implements Converter<T> {
    private final Type type;
    private final Gson gson;

    private GsonConverter(TypeToken<T> token, Gson gson) {
        this.type = token.getType();
        this.gson = gson;
    }

    @Override
    public void writeToStream(OutputStream stream, T content) {
        OutputStreamWriter osw = new OutputStreamWriter(stream, Util.UTF_8);
        gson.toJson(content, type, osw);
        try {
            osw.flush();
        } catch (IOException ignored) {
        }
    }

    @Override
    public T readFromStream(InputStream stream) throws IOException {
        InputStreamReader isr = new InputStreamReader(stream, Util.UTF_8);
        return gson.fromJson(isr, type);
    }

    private static final int MAX_CACHE_SIZE = 50;
    private static final LruCache<TypeToken<?>, GsonConverter<?>> CACHE = new LruCache<>(MAX_CACHE_SIZE);

    @SuppressWarnings("unchecked")
    static <T> GsonConverter<T> get(TypeToken<T> token, Gson gson) {
        GsonConverter<T> converter = (GsonConverter<T>) CACHE.get(token);
        if (converter == null) {
            converter = new GsonConverter<>(token, gson);
            CACHE.put(token, converter);
        }
        return converter;
    }

    static <T> GsonConverter<T> get(Class<T> clas, Gson gson) {
        return get(TypeToken.get(clas), gson);
    }
}
