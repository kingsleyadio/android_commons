package ng.kingsley.android.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2015
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseFragment extends Fragment {

    private Bundle savedViewState;
    protected AppCompatActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) activity;
        }
        if (savedInstanceState != null) {
            savedViewState = savedInstanceState.getBundle("savedViewState");
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedViewState != null) {
            onRestoreViewState(savedViewState);
        }
    }

    protected void onRestoreViewState(@NonNull Bundle savedViewState) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (savedViewState == null) {
            savedViewState = new Bundle();
        }
        onSaveViewState(savedViewState);
    }

    protected void onSaveViewState(@NonNull Bundle savedViewState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("savedViewState", savedViewState);
    }

    /**
     * Get the Main Application component for dependency injection.
     */
    protected <C> C getAppComponent(Class<C> componentType) {
        return componentType.cast(((BaseApplication) getActivity().getApplication()).getComponent());
    }
}
