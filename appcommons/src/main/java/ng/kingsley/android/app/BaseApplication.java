package ng.kingsley.android.app;

import android.app.Application;

import ng.kingsley.android.appcommons.R;
import ng.kingsley.android.dagger.components.ApplicationComponent;
import ng.kingsley.android.util.Inputs;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author ADIO Kingsley O.
 * @since 05 Jun, 2015
 */
public abstract class BaseApplication<T extends ApplicationComponent> extends Application {

    private T mComponent;
    private com.google.gson.Gson mGson;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = createComponent();
        mGson = createGson();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_default))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        AppManager.register(this);
    }

    protected com.google.gson.Gson createGson() {
        return new com.google.gson.GsonBuilder()
                .setDateFormat(Inputs.DATE_FORMAT_DEFAULT)
                .create();
    }

    public final com.google.gson.Gson getGson() {
        return mGson;
    }

    protected abstract T createComponent();

    public final T getComponent() {
        return this.mComponent;
    }
}
