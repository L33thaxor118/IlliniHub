package com.example.alanrgan.illinihub.util;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class GPSUtils {
  public static final double EARTH_RADIUS_MI = 6371.0 / 1.609344;

  public static double milesBetween(LatLng pt1, LatLng pt2) {
    double latitude = pt1.getLatitude();
    double longitude = pt1.getLongitude();
    double dLatitude = pt2.getLatitude();
    double dLongitude = pt2.getLongitude();

    double diffLatRadians = (latitude - dLatitude) * Math.PI / 180;
    double diffLonRadians = (longitude - dLongitude) * Math.PI / 180;

    double latRadians = latitude * Math.PI / 180;
    double dLatRadians = dLatitude * Math.PI / 180;

    double pt = Math.pow(Math.sin(diffLatRadians/2), 2) + Math.pow(Math.sin(diffLonRadians/2), 2)
        * Math.cos(latRadians) * Math.cos(dLatRadians);

    double distanceMi = 2 * Math.atan2(Math.sqrt(pt), Math.sqrt(1-pt)) * EARTH_RADIUS_MI;

    return distanceMi;
  }
}
