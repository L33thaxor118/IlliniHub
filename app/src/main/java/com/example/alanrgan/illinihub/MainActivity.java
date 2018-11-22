package com.example.alanrgan.illinihub;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;


public class MainActivity extends LocationActivity {
  private LocationStore locationStore;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    locationStore = new LocationStore();

    //Testing database connection
    Event e = new Event();
    db.eventDao().deleteAll();
    e.title = "test";
    e.description = "testing";
    e.latitude = 40.107;
    e.longitude = -88.227;
    e.tags = "none";
    db.eventDao().insertAll(e);
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

    //Creating marker with event retrieved from Database
    List<Event> dbEvents = db.eventDao().getAll();
    mapboxMap.addMarker(new MarkerOptions()
              .position(new LatLng(dbEvents.get(0).latitude, dbEvents.get(0).longitude))
              .title(dbEvents.get(0).title));

    // Start the polling after mapboxMap exists
    locationStore.run();
  }

}
