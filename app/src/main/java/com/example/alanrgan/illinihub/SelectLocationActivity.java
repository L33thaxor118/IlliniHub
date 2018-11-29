package com.example.alanrgan.illinihub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectLocationActivity extends LocationActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, View.OnClickListener, FilterDrawerFragment.OnFragmentInteractionListener {
  //Intent keys
  public static final String LOCATION_EXTRA = "select_location_location";
  public static final String TITLE_EXTRA = "select_location_title";
  public static final String DESC_EXTRA = "select_location_description";
  public static final String START_TIME_EXTRA = "select_location_start_time";
  public static final String END_TIME_EXTRA = "select_location_end_time";
  public static final String TAGS_EXTRA = "select_location_tags";

  //Intent data
  private long selectedStartDate;
  private long selectedEndDate;
  private double[] selectedLocation;
  private String selectedTitle;
  private String selectedDescription;
  private ArrayList<String> selectedTags;

  private MapView mapView;
  private MapboxMap mapboxMap;
  private Marker marker;
  private Button nextButton;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTitle("Select Event Location");
    super.onCreate(savedInstanceState);
    Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
    setContentView(R.layout.activity_select_location);

    Intent intent = getIntent();
    selectedTitle = intent.getStringExtra(CreateEventActivity.TITLE_EXTRA);
    selectedDescription = intent.getStringExtra(CreateEventActivity.DESC_EXTRA);
    selectedStartDate = intent.getLongExtra(CreateEventActivity.START_TIME_EXTRA, 0);
    selectedEndDate = intent.getLongExtra(CreateEventActivity.END_TIME_EXTRA, 0);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
    nextButton = (Button) findViewById(R.id.nextButton);
    nextButton.setOnClickListener(this);
  }

  @Override
  public void onLocationChanged(Location location) {

  }
  @Override
  public void updateFilter(List<String> tags){

  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    SelectLocationActivity.this.mapboxMap = mapboxMap;
    marker = mapboxMap.addMarker(new MarkerOptions()
            .position(new LatLng(40.107601,-88.227133)));

    mapboxMap.addOnMapClickListener(this);
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {
    ValueAnimator markerAnimator = ObjectAnimator.ofObject(marker, "position",
            new LatLngEvaluator(), marker.getPosition(), point);
    markerAnimator.setDuration(1000);
    markerAnimator.start();
  }

  @Override
  public void onClick(View v){
    if (v == nextButton){
      Intent intent = new Intent(this, SelectTagsActivity.class);
      intent.putExtra(TITLE_EXTRA, selectedTitle);
      intent.putExtra(DESC_EXTRA, selectedDescription);
      intent.putExtra(START_TIME_EXTRA, selectedStartDate);
      intent.putExtra(END_TIME_EXTRA, selectedEndDate);
      LatLng location = new LatLng(marker.getPosition());
      double loc[] = new double[2];
      loc[0] = location.getLatitude();
      loc[1] = location.getLongitude();
      intent.putExtra(LOCATION_EXTRA, loc);
      startActivityForResult(intent, 3);
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
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mapboxMap != null) {
      mapboxMap.removeOnMapClickListener(this);
    }
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  private static class LatLngEvaluator implements TypeEvaluator<LatLng> {
    private LatLng latLng = new LatLng();

    @Override
    public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
      latLng.setLatitude(startValue.getLatitude()
              + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
      latLng.setLongitude(startValue.getLongitude()
              + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
      return latLng;
    }
  }

  public void done(){
    Intent intent = new Intent();
    intent.putExtra(TITLE_EXTRA, selectedTitle);
    intent.putExtra(DESC_EXTRA, selectedDescription);
    intent.putExtra(START_TIME_EXTRA, selectedStartDate);
    intent.putExtra(END_TIME_EXTRA, selectedEndDate);
    intent.putExtra(LOCATION_EXTRA, selectedLocation);
    intent.putExtra(TAGS_EXTRA, selectedTags);
    setResult(RESULT_OK, intent);
    finish();
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 3) {
      if (resultCode == RESULT_OK) {
        selectedLocation = data.getDoubleArrayExtra(SelectTagsActivity.LOCATION_EXTRA);
        selectedTitle = data.getStringExtra(SelectTagsActivity.TITLE_EXTRA);
        selectedDescription = data.getStringExtra(SelectTagsActivity.DESC_EXTRA);
        selectedStartDate = data.getLongExtra(SelectTagsActivity.START_TIME_EXTRA, 0);
        selectedEndDate = data.getLongExtra(SelectTagsActivity.END_TIME_EXTRA, 0);
        selectedTags = data.getStringArrayListExtra(SelectTagsActivity.TAGS_EXTRA);
        done();
      }
    }
  }

}
