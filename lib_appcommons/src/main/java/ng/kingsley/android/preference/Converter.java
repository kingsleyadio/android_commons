package ng.kingsley.android.preference;

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

public interface Converter<T> {

    String toString(T content);

    T fromString(String serialized);
}
