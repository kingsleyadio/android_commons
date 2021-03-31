package ng.kingsley.android.app;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2015
 */
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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        onRestoreViewState(savedViewState);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("savedViewState", savedViewState);
    }

    protected final boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Get the Main Application component for dependency injection.
     */
    protected <C> C getAppComponent(Class<C> componentType) {
        return componentType.cast(((BaseApplication) requireActivity().getApplication()).getComponent());
    }
}
