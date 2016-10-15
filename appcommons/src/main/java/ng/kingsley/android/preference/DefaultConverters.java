package ng.kingsley.android.preference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

class DefaultConverters {

    private DefaultConverters() {
    }

    static class GsonConverter<T> implements Converter<T> {
        private Type type;
        private Gson gson;

        GsonConverter(Class<T> clas, Gson gson) {
            this.type = clas;
            this.gson = gson;
        }

        GsonConverter(TypeToken<T> token, Gson gson) {
            this.type = token.getType();
            this.gson = gson;
        }

        @Override
        public String toString(T content) {
            return gson.toJson(content, type);
        }

        @Override
        public T fromString(String serialized) {
            return gson.fromJson(serialized, type);
        }
    }
}
