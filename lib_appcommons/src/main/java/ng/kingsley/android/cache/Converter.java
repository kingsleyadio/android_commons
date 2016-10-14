package ng.kingsley.android.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

public interface Converter<T> {

    void writeToStream(OutputStream stream, T content) throws IOException;

    T readFromStream(InputStream stream) throws IOException;

}
