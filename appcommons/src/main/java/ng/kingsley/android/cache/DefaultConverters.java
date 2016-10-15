package ng.kingsley.android.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

class DefaultConverters {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final int BLOCK_SIZE = 4096;

    private DefaultConverters() {
    }

    static class StringConverter implements Converter<String> {
        @Override
        public void writeToStream(OutputStream stream, String content) throws IOException {
            byte[] bytes = content.getBytes(UTF_8);
            stream.write(bytes, 0, bytes.length);
        }

        @Override
        public String readFromStream(InputStream stream) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[BLOCK_SIZE];
            int l;
            while ((l = stream.read(bytes)) != -1) {
                baos.write(bytes, 0, l);
            }
            return new String(baos.toByteArray());
        }
    }

    static class GsonConverter<T> implements Converter<T> {
        private final Type type;
        private final Gson gson;

        GsonConverter(Class<T> clas, Gson gson) {
            this.type = clas;
            this.gson = gson;
        }

        GsonConverter(TypeToken<T> token, Gson gson) {
            this.type = token.getType();
            this.gson = gson;
        }

        @Override
        public void writeToStream(OutputStream stream, T content) {
            OutputStreamWriter osw = new OutputStreamWriter(stream, UTF_8);
            gson.toJson(content, type, osw);
        }

        @Override
        public T readFromStream(InputStream stream) throws IOException {
            InputStreamReader isr = new InputStreamReader(stream, UTF_8);
            return gson.fromJson(isr, type);
        }
    }

    static class InputStreamConverter implements Converter<InputStream> {
        @Override
        public void writeToStream(OutputStream stream, InputStream content) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[BLOCK_SIZE];
            int l;
            while ((l = content.read(bytes)) != -1) {
                baos.write(bytes, 0, l);
            }
            baos.writeTo(stream);
        }

        @Override
        public InputStream readFromStream(InputStream stream) throws IOException {
            return stream;
        }
    }

    static class ByteArrayConverter implements Converter<byte[]> {

        @Override
        public void writeToStream(OutputStream stream, byte[] content) throws IOException {
            stream.write(content, 0, content.length);
        }

        @Override
        public byte[] readFromStream(InputStream stream) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[BLOCK_SIZE];
            int l;
            while ((l = stream.read(bytes)) != -1) {
                baos.write(bytes, 0, l);
            }
            return baos.toByteArray();
        }
    }

}
