package com.example.alanrgan.illinihub.util;

import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class CircleBuilder {
  private static final double MI_TO_KM_FACTOR = 1.609344;
  // Number of points to add to the polygon
  private static final int N_POINTS = 64;

  public static PolygonOptions create(LatLng center, double radius) {
    if (radius <= 0.0) {
      throw new IllegalArgumentException("@param radius must be non-negative");
    }

    PolygonOptions polygonOptions = new PolygonOptions();
    polygonOptions.addAll(generateCircleCoords(center, radius));
    return polygonOptions;
  }

  // Adapted from https://github.com/mapbox/mapbox-gl-native/issues/2167#issuecomment-334745822
  private static List<LatLng> generateCircleCoords(LatLng center, double radius) {
    double latitude = center.getLatitude();
    double longitude = center.getLongitude();

    List<LatLng> circleLatLngList = new ArrayList<>();
    double km = MI_TO_KM_FACTOR * radius;

    double distRadians = km / 6371.0;
    double centerLatRadians = latitude * Math.PI / 180.0;
    double centerLonRadians = longitude * Math.PI / 180.0;

    for (int i = 0; i < N_POINTS; i++) {
      double theta = ((double)i / N_POINTS) * (2 * Math.PI);
      double pointLatRadians = Math.asin(Math.sin(centerLatRadians) * Math.cos(distRadians) +
        Math.cos(centerLatRadians) * Math.sin(distRadians) * Math.cos(theta));
      double pointLonRadians = centerLonRadians + Math.atan2(Math.sin(theta) * Math.sin(distRadians)
        * Math.cos(centerLatRadians), Math.cos(distRadians) - Math.sin(centerLatRadians) * Math.sin(pointLatRadians));
      double pointLat = pointLatRadians * 180 / Math.PI;
      double pointLon = pointLonRadians * 180 / Math.PI;
      circleLatLngList.add(new LatLng(pointLat, pointLon));
    }

    circleLatLngList.add(circleLatLngList.get(0));


    return circleLatLngList;
  }
}
