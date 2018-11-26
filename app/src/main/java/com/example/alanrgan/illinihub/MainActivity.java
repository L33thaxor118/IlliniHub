package com.example.alanrgan.illinihub;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.example.alanrgan.illinihub.util.CircleBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

// MainActivity MUST implement FilterDrawerFragment listener interface
// in order to communicate events
public class MainActivity extends LocationActivity implements FilterDrawerFragment.OnFragmentInteractionListener {
  private LocationStore locationStore;
  private RadiusActionBar radiusActionBar;
  private SlideUp slideUp;
  private MapboxMap map;
  private View filterDrawer;
  private Polygon discoveryCircle;

  // Temporary variables to demonstrate interaction between fragment and MainActivity
  private Marker quadMarker;

  private enum MarkerColor {
    BLUE,
    GREEN,
    RED,
    PURPLE,
    YELLOW
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    locationStore = new LocationStore();

    radiusActionBar = findViewById(R.id.radiusActionBar);
    initializeSlideUp();
    initializeFabs();

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
      runOnUiThread(() -> addMarker("Async marker", coord, MarkerColor.BLUE));
    });

    //Creating marker with event retrieved from Database
    List<Event> dbEvents = db.eventDao().getAll();
    addMarker(dbEvents.get(0));

    radiusActionBar.onRadiusChange((obs, radius) -> renderDiscoveryRadius(radius));

    // Start the polling after mapboxMap exists
    locationStore.run();
  }

  @Override
  public void onLocationChanged(Location location) {
    // TODO: Render discovery radius here
    renderDiscoveryRadius(location);
  }

  private void renderDiscoveryRadius(double radius) {
    renderDiscoveryRadius(locationComponent.getLastKnownLocation(), radius);
  }

  private void renderDiscoveryRadius(Location location) {
    double radius = radiusActionBar.getRadius();
    renderDiscoveryRadius(location, radius);
  }

  private void renderDiscoveryRadius(Location location, double radius) {
    if (discoveryCircle != null) {
      map.removePolygon(discoveryCircle);
    }

    PolygonOptions circleOptions = CircleBuilder
        .create(new LatLng(location), radius)
        .fillColor(Color.argb(125, 53, 64, 255));
    discoveryCircle = map.addPolygon(circleOptions);
  }

  /**
   * Add a marker corresponding to an event to the map.
   *
   * @param event the Event to draw a marker for
   */
  private void addMarker(Event event) {
    // TODO: Specifiy a MarkerColor attribute for each event, or convert the event category
    // to a MarkerColor
    addMarker(event.title, new LatLng(event.latitude, event.longitude));
  }

  private Marker addMarker(String title, LatLng position) {
    return addMarker(title, position, MarkerColor.RED);
  }

  /**
   * Add a marker onto the map with a specified title, position and color.
   *
   * See {MarkerColor} for available colors. Default color is MarkerColor.RED, which does not
   * have an associated drawable resource file.
   *
   * @param title Title text to display on the marker
   * @param position GPS coordinates at which the marker will be placed
   * @param color Color of the marker
   * @return Rendered Mapbox marker
   */
  private Marker addMarker(String title, LatLng position, MarkerColor color) {
    int drawableId = -1;

    switch (color) {
      case BLUE:
        drawableId = R.mipmap.ic_blue_marker;
        break;
      case GREEN:
        drawableId = R.mipmap.ic_green_marker;
        break;
      case PURPLE:
        drawableId = R.mipmap.ic_purple_marker;
        break;
      case YELLOW:
        drawableId = R.mipmap.ic_yellow_marker;
        break;
      // Default option is a red marker
      default:
        break;
    }

    MarkerOptions markerOptions = new MarkerOptions()
        .title(title)
        .position(position);

    if (drawableId >= 0) {
      IconFactory iconFactory = IconFactory.getInstance(this);
      Icon icon = iconFactory.fromResource(drawableId);
      markerOptions.icon(icon);
    }

    return map.addMarker(markerOptions);
  }

  private void addMainMarker() {
    quadMarker = addMarker("Main Quad", new LatLng(40.107601, -88.227133));
  }

  /**
   * Initialize sliding drawer view.
   */
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

  private void initializeFabs() {
    FloatingActionButton recenterButton = findViewById(R.id.recenterButton);
    recenterButton.setOnClickListener(event -> map.animateCamera(m ->
        new CameraPosition.Builder()
            .target(new LatLng(locationComponent.getLastKnownLocation()))
            .zoom(17)
            .build()
    ));
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
