package ng.kingsley.android.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import ng.kingsley.android.appcommons.R;

/**
 * @author ADIO Kingsley O.
 * @since 05 Jun, 2015
 */
public class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;
    protected DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        trySetupToolbar();
        trySetupDrawer();
    }

    protected void trySetupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected boolean trySetupDrawer() {
        mDrawer = findViewById(R.id.drawer);
        if (mDrawer != null) {
            View side = mDrawer.findViewById(R.id.navigation_view);
            int possibleMinDrawerWidth = getScreenSize(this)[0] - attrToDimenPx(this, R.attr.actionBarSize);
            int maxDrawerWidth = getResources().getDimensionPixelSize(R.dimen.navigation_drawer_max_width);
            side.getLayoutParams().width = Math.min(possibleMinDrawerWidth, maxDrawerWidth);

            mToggle = new ActionBarDrawerToggle(this,
                    mDrawer, R.string.app_name, R.string.app_name);
            mToggle.setDrawerIndicatorEnabled(true);
            mDrawer.addDrawerListener(mToggle);
            mDrawer.post(new Runnable() {
                @Override
                public void run() {
                    mToggle.syncState();
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    }
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle != null && mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Force checking the native fragment manager for a backstack rather than
        // the support lib fragment manager.
        if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (!getFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }

    protected void presentFragment(Fragment fragment, boolean addToBackstack, boolean detachExisting) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment);
        if (detachExisting) {
            transaction.detach(getFragmentManager().findFragmentById(R.id.container));
        }
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    protected void presentFragment(android.support.v4.app.Fragment fragment,
            boolean addToBackstack, boolean detachExisting) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment);
        if (detachExisting) {
            transaction.detach(getSupportFragmentManager().findFragmentById(R.id.container));
        }
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    protected boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    private static int attrToDimenPx(Context context, int attr) {
        TypedArray a = null;
        try {
            a = context.getTheme().obtainStyledAttributes(new int[]{attr});
            return a.getDimensionPixelSize(0, 0);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    private static int[] getScreenSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    /**
     * Get the Main Application component for dependency injection.
     */
    protected <C> C getAppComponent(Class<C> componentType) {
        return componentType.cast(((BaseApplication) getApplication()).getComponent());
    }
}
