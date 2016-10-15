package ng.kingsley.android.app;

import android.app.Service;

/**
 * @author ADIO Kingsley O.
 * @since 02 Oct, 2015
 */
public abstract class BaseService extends Service {

    /**
     * Get the Main Application component for dependency injection.
     */
    protected <C> C getAppComponent(Class<C> componentType) {
        return componentType.cast(((BaseApplication) getApplication()).getComponent());
    }
}
