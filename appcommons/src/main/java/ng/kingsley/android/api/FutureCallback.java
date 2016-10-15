package ng.kingsley.android.api;

/**
 * @author ADIO Kingsley O.
 * @since 22 Apr, 2015
 */
public interface FutureCallback<T> {
    void onCompleted(Exception e, T result);
}
