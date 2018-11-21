package com.example.alanrgan.illinihub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class MainActivity extends LocationActivity {
  private LocationStore locationStore;
  private SlideUp slideUp;
  private View slideView;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    locationStore = new LocationStore();

    initializeSlideUp();
  }

  @Override
  public void onMapReady(final MapboxMap mapboxMap) {
    // The map will be created AFTER the user grants location permissions
    mapboxMap.addMarker(new MarkerOptions()
        .position(new LatLng(40.107601, -88.227133))
        .title("Main Quad"));

    // Register a listener for async data loading
    locationStore.onDataUpdated((obj, coord) -> {
      // All UI modifications need to be run on the UI thread with
      // the following function
      runOnUiThread(() -> mapboxMap.addMarker(
        new MarkerOptions()
          .position(coord)
          .title("Async marker")
      ));
    });

    // Start the polling after mapboxMap exists
    locationStore.run();
  }

  private void initializeSlideUp() {
    slideView = findViewById(R.id.slideView);
    slideUp = new SlideUpBuilder(slideView)
          .withStartState(SlideUp.State.HIDDEN)
          .withStartGravity(Gravity.BOTTOM)
          // 'Slide from other view' means that a slide can be triggered
          // by another view, (e.g. a handle), rather than a button.
          // We will use a persistent view at the bottom of the map
          // to let the user drag to show/hide the view.
          .withSlideFromOtherView(findViewById(R.id.bottomSlideHandle))
          .build();
  }

}
