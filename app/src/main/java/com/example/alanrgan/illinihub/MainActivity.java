package com.example.alanrgan.illinihub;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

// MainActivity MUST implement FilterDrawerFragment listener interface
// in order to communicate events
public class MainActivity extends LocationActivity implements FilterDrawerFragment.OnFragmentInteractionListener {
  private LocationStore locationStore;
  private SlideUp slideUp;
  private MapboxMap map;
  private View filterDrawer;

  // Temporary variables to demonstrate interaction between fragment and MainActivity
  private Marker quadMarker;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    locationStore = new LocationStore();

    initializeSlideUp();

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
    super.onMapReady(mapboxMap);

    map = mapboxMap;
    // The map will be created AFTER the user grants location permissions
    addMainMarker();

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

  @Override
  public void onLocationChanged(Location location) {
    // TODO: Render discovery radius here
    System.out.println("Location changed to " + location.toString());
  }

  private void addMainMarker() {
    quadMarker = map.addMarker(new MarkerOptions()
        .position(new LatLng(40.107601, -88.227133))
        .title("Main Quad"));
  }

  private void initializeSlideUp() {
    filterDrawer = findViewById(R.id.filterDrawer);
    slideUp = new SlideUpBuilder(filterDrawer)
          .withStartState(SlideUp.State.HIDDEN)
          .withStartGravity(Gravity.BOTTOM)
          // 'Slide from other view' means that a slide can be triggered
          // by another view, (e.g. a handle), rather than a button.
          // We will use a persistent view at the bottom of the map
          // to let the user drag to show/hide the view.
          .withSlideFromOtherView(findViewById(R.id.bottomSlideHandle))
          .build();
  }

  // Proof of concept for interaction between a Fragment and the Main Activity
  // This function will toggle the Main Quad marker
  @Override
  public void onFragmentInteraction(String param) {
    if (map == null) return;
    if (quadMarker != null) {
      long markerId = quadMarker.getId();
      if (map.getAnnotation(markerId) != null) {
        map.removeMarker(quadMarker);
      } else {
        addMainMarker();
      }
    }
  }
}
