package com.example.alanrgan.illinihub;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.alanrgan.illinihub.util.GPSUtils;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Date;

@Entity
public class Event {

  public Event(String title, String description, double latitude, double longitude, Date startTime, Date endTime, String visibility) {
    this.title = title;
    this.description = description;
    this.latitude = latitude;
    this.longitude = longitude;
    this.startTime = startTime;
    this.endTime = endTime;
    this.visibility = visibility;
    this.tags = "";
  }

  public Event() {
  }

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

  @ColumnInfo(name = "StartTime")
  public Date startTime;

  @ColumnInfo(name = "EndTime")
  public Date endTime;

  @ColumnInfo(name = "Visibility")
  public String visibility;

  public double distanceFrom(LatLng coord) {
    return GPSUtils.milesBetween(new LatLng(latitude, longitude), coord);
  }
}
