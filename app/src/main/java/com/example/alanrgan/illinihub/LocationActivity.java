package com.example.alanrgan.illinihub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import com.example.alanrgan.illinihub.db.DatabaseHelper;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;


import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;

import java.util.List;


public abstract class LocationActivity extends AppCompatActivity implements
        OnMapReadyCallback, OnLocationClickListener, PermissionsListener,
        OnCameraTrackingChangedListener, MapboxMap.OnMapLongClickListener,
        MapboxMap.OnMapClickListener {

  private PermissionsManager permissionsManager;
  protected MapView mapView;
  private MapboxMap mapboxMap;
  protected LocationComponent locationComponent;
  private boolean isInTrackingMode;
  protected DatabaseHelper dbHelper;
  protected AlertDialog markerAlert;
  private LocationUpdater locationUpdater;
  protected FragmentManager fragmentManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fragmentManager = getSupportFragmentManager();
    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    dbHelper = new DatabaseHelper(getApplicationContext());
  }

  @Override
  public void onMapReady(@NonNull MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
      @Override
      public void onStyleLoaded(@NonNull Style style) {
        UiSettings settings = mapboxMap.getUiSettings();
        settings.setCompassGravity(Gravity.LEFT);
        enableLocationComponent(style);
      }
    });

  }

  @SuppressWarnings( {"MissingPermission"})
  private void enableLocationComponent(@NonNull Style loadedMapStyle) {
    // Check if permissions are enabled and if not request
    if (PermissionsManager.areLocationPermissionsGranted(this)) {

      // Create and customize the LocationComponent's options
      LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
              .elevation(5)
              .accuracyAlpha(.0f)
              .accuracyColor(Color.RED)
              .build();

      // Get an instance of the component
      locationComponent = mapboxMap.getLocationComponent();
      Log.d("LOCATION", "location component loaded");

      LocationComponentActivationOptions locationComponentActivationOptions =
              LocationComponentActivationOptions.builder(this, loadedMapStyle)
                      .locationComponentOptions(customLocationComponentOptions)
                      .build();

      // Activate with options
      locationComponent.activateLocationComponent(locationComponentActivationOptions);

      // Enable to make component visible
      locationComponent.setLocationComponentEnabled(true);

      // Set the component's camera mode
      locationComponent.setCameraMode(CameraMode.TRACKING);

      // Set the component's render mode
      locationComponent.setRenderMode(RenderMode.COMPASS);

      // Add the location icon click listener
      locationComponent.addOnLocationClickListener(this);

      // Add the camera tracking listener. Fires if the map camera is manually moved.
      locationComponent.addOnCameraTrackingChangedListener(this);

      locationUpdater = new LocationUpdater(this);


    } else {
      permissionsManager = new PermissionsManager(this);
      permissionsManager.requestLocationPermissions(this);
    }
  }


  @SuppressWarnings( {"MissingPermission"})
  @Override
  public void onLocationComponentClick() {
    if (locationComponent.getLastKnownLocation() != null) {
      Toast.makeText(this, String.format("location",
              locationComponent.getLastKnownLocation().getLatitude(),
              locationComponent.getLastKnownLocation().getLongitude()), Toast.LENGTH_LONG).show();
    }
  }


  @Override
  public boolean onMapLongClick(LatLng Point) {
    return false;
  };

  protected void onLocationUpdated(LocationEngineResult result) {
    Location location = result.getLastLocation();

    if (location == null) {
      return;
    }

    Log.d("LOCATION ENGINE UPDATE:", "lat: " +
            String.valueOf(result.getLastLocation().getLatitude()) +
            " long: " + String.valueOf(result.getLastLocation().getLongitude()));

    // Pass the new location to the Maps SDK's LocationComponent
    if (result.getLastLocation() != null) {
      mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
    }
  }

  protected void onLocationUpdateFailure(@NonNull Exception exception) {
    Log.d("LocationChangeActivity", exception.getLocalizedMessage());
    Toast.makeText(this, "Location update failed",
            Toast.LENGTH_SHORT).show();
  }

  @Override
  public boolean onMapClick(LatLng Point) {
    return false;
  };

  @Override
  public void onCameraTrackingDismissed() {
    isInTrackingMode = false;
  }

  @Override
  public void onCameraTrackingChanged(int currentMode) {
    // Empty on purpose
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override
  public void onExplanationNeeded(List<String> permissionsToExplain) {
    Toast.makeText(this, "give me dat", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPermissionResult(boolean granted) {
    if (granted) {
      mapboxMap.getStyle(new Style.OnStyleLoaded() {
        @Override
        public void onStyleLoaded(@NonNull Style style) {
          enableLocationComponent(style);
        }
      });
    } else {
      Toast.makeText(this, "not granted", Toast.LENGTH_LONG).show();
      finish();
    }
  }

  @SuppressWarnings( {"MissingPermission"})
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
        if (markerAlert != null) {
      markerAlert.dismiss();
    }
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  private class LocationUpdater implements LocationEngineCallback<LocationEngineResult> {
    private LocationActivity locationActivity;
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 100L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

    public LocationUpdater(LocationActivity activity) {
      locationActivity = activity;
      initLocationEngine();
    }
    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
      locationEngine = LocationEngineProvider.getBestLocationEngine(locationActivity);

      LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
              .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
              .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

      locationEngine.requestLocationUpdates(request, this, getMainLooper());
      locationEngine.getLastLocation(this);
    }

    /**
     * The LocationEngineCallback interface's method which fires when the device's location has changed.
     *
     * @param result the LocationEngineResult object which has the last known location within it.
     */
    @Override
    public void onSuccess(LocationEngineResult result) {
      locationActivity.onLocationUpdated(result);
    }

    /**
     * The LocationEngineCallback interface's method which fires when the device's location can not be captured
     *
     * @param exception the exception message
     */
    @Override
    public void onFailure(@NonNull Exception exception) {
      locationActivity.onLocationUpdateFailure(exception);
    }

  }

}