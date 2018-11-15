package com.example.alanrgan.illinihub;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mapbox.mapboxsdk.Mapbox;

public class MainActivity extends Activity {
    private MapWrapper mapWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        mapWrapper = new MapWrapper(this, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapWrapper.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapWrapper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapWrapper.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapWrapper.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapWrapper.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapWrapper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapWrapper.onSaveInstanceState(outState);
    }
}
