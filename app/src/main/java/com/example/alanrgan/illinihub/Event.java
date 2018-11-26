package com.example.alanrgan.illinihub;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.alanrgan.illinihub.util.GPSUtils;
import com.mapbox.mapboxsdk.geometry.LatLng;

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
    return GPSUtils.milesBetween(new LatLng(latitude, longitude), coord);
  }
}
