package ng.kingsley.android.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            onRestoreViewState(savedViewState);
        }
    }

    protected void onRestoreViewState(@Nullable Bundle savedViewState) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (getView() != null) {
            if (savedViewState == null) savedViewState = new Bundle();
            onSaveViewState(savedViewState);
        }
    }

    protected void onSaveViewState(@NonNull Bundle savedViewState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("savedViewState", savedViewState);
    }

    protected boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission)
          == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Get the Main Application component for dependency injection.
     */
    protected <C> C getAppComponent(Class<C> componentType) {
        return componentType.cast(((BaseApplication) getActivity().getApplication()).getComponent());
    }
}
