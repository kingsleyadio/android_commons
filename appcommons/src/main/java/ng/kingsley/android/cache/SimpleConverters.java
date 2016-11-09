package ng.kingsley.android.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

class SimpleConverters {

    private SimpleConverters() {
    }

    static Converter<String> STRING = new Converter<String>() {
        @Override
        public void writeToStream(OutputStream stream, String content) throws IOException {
            byte[] bytes = content.getBytes(Util.UTF_8);
            stream.write(bytes, 0, bytes.length);
        }

        @Override
        public String readFromStream(InputStream stream) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Util.moveStream(stream, baos);
            return new String(baos.toByteArray(), Util.UTF_8);
        }
    };

    static Converter<InputStream> INPUT_STREAM = new Converter<InputStream>() {
        @Override
        public void writeToStream(OutputStream stream, InputStream content) throws IOException {
            Util.moveStream(content, stream);
        }

        @Override
        public InputStream readFromStream(InputStream stream) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Util.moveStream(stream, baos);
            return new ByteArrayInputStream(baos.toByteArray());
        }
    };

    static Converter<byte[]> BYTE_ARRAY = new Converter<byte[]>() {

        @Override
        public void writeToStream(OutputStream stream, byte[] content) throws IOException {
            stream.write(content, 0, content.length);
        }

        @Override
        public byte[] readFromStream(InputStream stream) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[Util.BLOCK_SIZE];
            int l;
            while ((l = stream.read(bytes)) != -1) {
                baos.write(bytes, 0, l);
            }
            return baos.toByteArray();
        }
    };

}
