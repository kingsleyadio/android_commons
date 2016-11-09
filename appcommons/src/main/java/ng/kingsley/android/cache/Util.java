package ng.kingsley.android.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author ADIO Kingsley O.
 * @since 09 Nov, 2016
 */

class Util {
    static final Charset UTF_8 = Charset.forName("UTF-8");
    static final int BLOCK_SIZE = 4096;

    static void moveStream(InputStream src, OutputStream dest) throws IOException {
        byte[] bytes = new byte[BLOCK_SIZE];
        int l;
        while ((l = src.read(bytes)) != -1) {
            dest.write(bytes, 0, l);
        }
    }
}
