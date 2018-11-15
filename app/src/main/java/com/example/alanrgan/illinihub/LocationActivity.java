package com.example.alanrgan.illinihub;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public abstract class LocationActivity extends Activity implements OnMapReadyCallback {
  protected MapView mapView;
  private Bundle savedState;
  private final int FINE_LOCATION_PERMISSION = 0;

  private void initMapView() {
    Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

    setContentView(R.layout.activity_main);

    /* Map: This represents the map in the application. */
    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedState);
    mapView.getMapAsync(this);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    savedState = savedInstanceState;
    // Check if we need to prompt the user for permissions
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      // Request fine location permissions if the app does not already have them
      ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0);
    } else {
      // If permissions are already granted, initialize the map view
      initMapView();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case FINE_LOCATION_PERMISSION:
        // Check if the user has granted the app location permissions
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          initMapView();
        }
      default:
        break;
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
}
