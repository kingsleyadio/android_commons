package com.kingsleyadio.appcommons.bootstrap;

import android.app.Activity;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class ShutdownCompatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.finishAffinity(this);
    }
}
