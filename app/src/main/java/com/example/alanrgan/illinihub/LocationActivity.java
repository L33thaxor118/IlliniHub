package com.example.alanrgan.illinihub;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.UiSettings;

// In order to use fragments, we need to use AppCompatActivity or FragmentActivity
public abstract class LocationActivity extends AppCompatActivity
    implements OnMapReadyCallback, LocationEngineListener {
  protected MapView mapView;
  private Bundle savedState;
  private final int FINE_LOCATION_PERMISSION = 0;
  protected Database db;

  protected LocationComponent locationComponent;
  protected LocationEngine locationEngine;

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
    db = Database.getDatabase(getApplicationContext());
    // Check if we need to prompt the user for permissions
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      // Request fine location permissions if the app does not already have them
      ActivityCompat.requestPermissions(
          this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    } else {
      // If permissions are already granted, initialize the map view
      initMapView();
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {
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
  public void onConnected() {
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onMapReady(final MapboxMap mapboxMap) {
    UiSettings settings = mapboxMap.getUiSettings();
    settings.setCompassGravity(Gravity.LEFT);

    locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
    locationEngine.addLocationEngineListener(this);
    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
    locationEngine.activate();

    locationComponent = mapboxMap.getLocationComponent();
    locationComponent.activateLocationComponent(this);
    locationComponent.setLocationComponentEnabled(true);
    locationComponent.setCameraMode(CameraMode.TRACKING);
    locationComponent.setRenderMode(RenderMode.NORMAL);
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
