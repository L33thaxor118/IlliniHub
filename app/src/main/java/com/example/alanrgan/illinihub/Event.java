package com.example.alanrgan.illinihub;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Event {
  @PrimaryKey(autoGenerate = true)
  public int eventId;

  @ColumnInfo(name = "Title")
  public String title;

  @ColumnInfo(name = "Description")
  public String description;

  @ColumnInfo(name = "Latitude")
  public double latitude;

  @ColumnInfo(name = "Longitude")
  public double longitude;

  @ColumnInfo(name = "tags(json)")
  public String tags;

  public double distanceFrom(LatLng coord) {
    double earthRadiusMi = 6371.0 / 1.609344;

    double dLatitude = coord.getLatitude();
    double dLongitude = coord.getLongitude();

    double diffLatRadians = (latitude - dLatitude) * Math.PI / 180;
    double diffLonRadians = (longitude - dLongitude) * Math.PI / 180;

    double latRadians = latitude * Math.PI / 180;
    double dLatRadians = dLatitude * Math.PI / 180;

    double pt = Math.pow(Math.sin(diffLatRadians/2), 2) + Math.pow(Math.sin(diffLonRadians/2), 2)
        * Math.cos(latRadians) * Math.cos(dLatRadians);

    double distanceMi = 2 * Math.atan2(Math.sqrt(pt), Math.sqrt(1-pt)) * earthRadiusMi;

    return distanceMi;
  }
}
