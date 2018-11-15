package com.example.alanrgan.illinihub;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class MainActivity extends LocationActivity {

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    // The map will be created AFTER the user grants location permissions
    mapboxMap.addMarker(new MarkerOptions()
        .position(new LatLng(40.107601, -88.227133))
        .title("Main Quad"));
  }
}
