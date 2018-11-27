package com.example.alanrgan.illinihub;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.example.alanrgan.illinihub.util.CircleBuilder;
import com.example.alanrgan.illinihub.util.GPSUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.alanrgan.illinihub.util.DBHelperAsyncResponse;

import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// MainActivity MUST implement FilterDrawerFragment listener interface
// in order to communicate events
public class MainActivity extends LocationActivity implements FilterDrawerFragment.OnFragmentInteractionListener, DBHelperAsyncResponse {
  private LocationStore locationStore;
  private RadiusActionBar radiusActionBar;
  private SlideUp slideUp;
  private MapboxMap map;
  private View filterDrawer;
  private Polygon discoveryCircle;
  private List<Event> eventsWithinRadius = new ArrayList<>();
  private Map<Long, Event> markerIdToEvent = new HashMap<>();

  private NotificationManager notificationManager;

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
    notificationManager = new NotificationManager(getApplicationContext());

    radiusActionBar = findViewById(R.id.radiusActionBar);
    initializeSlideUp();
    initializeFabs();

    dbHelper.populateWithSampleData();
  }

  @Override
  public void onMapReady(final MapboxMap mapboxMap) {
    super.onMapReady(mapboxMap);

    map = mapboxMap;

    // The map will be created AFTER the user grants location permissions

    // Register a listener for async data loading
    locationStore.onDataUpdated((obj, coord) -> {
      // All UI modifications need to be run on the UI thread with
      // the following function
      runOnUiThread(() -> addMarker("Async marker", coord, MarkerColor.BLUE));
    });

    //Creating markers with events retrieved from Database
    List<Event> dbEvents = dbHelper.getAll();
    for (int i = 0; i < dbEvents.size(); i++) {
      addMarker(dbEvents.get(i));
    }

    radiusActionBar.onRadiusChange((obs, radius) -> renderDiscoveryRadius(radius));

    // Register radius refinement listener
    map.addOnMapLongClickListener(this::updateRadiusToPoint);

    map.setOnMarkerClickListener(marker -> {
      Event event = markerIdToEvent.get(marker.getId());
      if (event == null) return false;

      System.out.println("Clicked on event " + event.title);

      return false;
    });

    // Start the polling after mapboxMap exists
    locationStore.run();
  }

  @Override
  public void onLocationChanged(Location location) {
    renderDiscoveryRadius(location);
  }

  private void updateRadiusToPoint(LatLng point) {
    Location location = locationComponent.getLastKnownLocation();
    double radius = GPSUtils.milesBetween(point, new LatLng(location));
    if (radius <= RadiusActionBar.MAXIMUM_ALLOWED_RADIUS) {
      renderDiscoveryRadius(location, radius);
    }
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
    radiusActionBar.updateRadius(radius);

    eventsWithinRadius.clear();

    // Each time the radius is redrawn, we must search through all events
    // and determine which ones are within the radius and populate the list accordingly
    dbHelper.getAll().forEach(event -> {
      double distFromUser = event.distanceFrom(new LatLng(location));
      if (distFromUser <= radius) {
        eventsWithinRadius.add(event);
      }
    });

    if (eventsWithinRadius.size() > 0) {
      // TODO: Create an 'EventDetails' activity where users can scroll through event info
      // We will want to pass in an ArrayList of Events to the intent. We will also
      // need to have Event implement the Serializable interface.

      // Right now, users are notified every time the circle is redrawn (which happens every time
      // the user moves). We only want to notify them once.
      // TODO: Keep track of whether or not a user has been notified about a specific event

      Intent intent = new Intent(getApplicationContext(), MainActivity.class);
      String title;
      String content;
      if (eventsWithinRadius.size() == 1) {
        Event event = eventsWithinRadius.get(0);
        title = "Found a nearby event";
        content = String.format("[%s]", event.title);
      } else {
        title = String.format("Found %d nearby events", eventsWithinRadius.size());
        content = "Tap to view event details";
      }

      notificationManager.showNotification(title, content, intent);
    }
  }

  /**
   * Add a marker corresponding to an event to the map.
   *
   * @param event the Event to draw a marker for
   */
  private Marker addMarker(Event event) {
    // TODO: Specify a MarkerColor attribute for each event, or convert the event category
    // to a MarkerColor
    Marker marker = addMarker(event.title, new LatLng(event.latitude, event.longitude));
    markerIdToEvent.put(marker.getId(), event);
    return marker;
  }

  private Marker addMarker(String title, LatLng position) {
    return addMarker(title, position, MarkerColor.RED);
  }

  /**
   * Add a marker onto the map with a specified title, position and color.
   * <p>
   * See {MarkerColor} for available colors. Default color is MarkerColor.RED, which does not
   * have an associated drawable resource file.
   *
   * @param title    Title text to display on the marker
   * @param position GPS coordinates at which the marker will be placed
   * @param color    Color of the marker
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

  private void removeMarker(Marker m) {
    if (m != null) {
      long markerId = m.getId();
      if (map.getAnnotation(markerId) != null) {
        map.removeMarker(m);
        markerIdToEvent.remove(markerId);
      }
    }
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
    recenterButton.setOnClickListener(event -> {
      double currentZoom = map.getCameraPosition().zoom;
      map.easeCamera(m ->
          new CameraPosition.Builder()
              .target(new LatLng(locationComponent.getLastKnownLocation()))
              .zoom(currentZoom)
              .build()
      );
    });

    FloatingActionButton createEventButton = findViewById(R.id.createEventButton);
    createEventButton.setOnClickListener(event -> {
      // TODO: Launch / inflate CreateEvent view here
    });
  }

  /**
   * This method is called by FilterDrawerFragment when the ArrayList containing current tags is updated.
   * @param tags
   */
  @Override
  public void updateFilter(List<String> tags) {
    map.clear();
    //dbHelper.getMatchesAsync(search.toString(), this);
    String query = generateQuery(tags, 0);
    List<Event> newEvents = dbHelper.getMatchingEvents(query);
    for (Event e : newEvents){
      addMarker(e);
    }
  }

  public String generateQuery(List<String> tags, int level) {
    if (tags.size() == 0){
      return "SELECT * FROM event";
    }
    StringBuilder query = new StringBuilder();
    if (level != 0) query.append("(SELECT * FROM ");
    else query.append("SELECT * FROM ");
    if (level < (tags.size() - 1)) {
      query.append(generateQuery(tags, level + 1));
    }
    if (level == 0) query.append("event INNER JOIN eventtagjoin ON event.eventId=eventtagjoin.event_id WHERE eventtagjoin.tag_name = '"+tags.get(level)+"' ");
    else query.append("event INNER JOIN eventtagjoin ON event.eventId=eventtagjoin.event_id WHERE eventtagjoin.tag_name = '"+tags.get(level)+"') ");
    return query.toString();
  }

  /**
   * Part of DBHelperAsyncResponse interface. Returns result of query
   * @param matches
   */
  @Override
  public void processFinishGetMatches(List<Event> matches) {
    for (int i = 0; i < matches.size(); i++) {
      addMarker(matches.get(i));
    }
  }
}
