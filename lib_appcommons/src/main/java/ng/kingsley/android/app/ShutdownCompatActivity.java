package ng.kingsley.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class ShutdownCompatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.finishAffinity(this);
    }
}
