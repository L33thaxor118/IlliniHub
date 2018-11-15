package com.example.alanrgan.illinihub;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MapWrapper {
    private MapView mapView;

    /**
     * Constructor
     * @param activity This allows to access Activity members such as findeViewById.
     * @param savedInstanceState
     */
    MapWrapper(Activity activity, Bundle savedInstanceState){
        mapView = (MapView) activity.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // Customize map with markers, polylines, etc.
            }
        });
    }

    void onStart() {
        mapView.onStart();
    }
    void onResume() {
        mapView.onResume();
    }

    void onPause() {
        mapView.onPause();
    }

    void onStop() {
        mapView.onStop();
    }

    void onLowMemory() {
        mapView.onLowMemory();
    }
    void onDestroy() {
        mapView.onDestroy();
    }
    void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }

}
