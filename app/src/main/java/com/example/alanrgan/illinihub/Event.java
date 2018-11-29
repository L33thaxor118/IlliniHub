package com.example.alanrgan.illinihub;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.alanrgan.illinihub.util.GPSUtils;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;

import java.util.Date;

@Entity
public class Event implements Serializable {
  public Event(String title, String description, double latitude, double longitude,
               Date startTime, Date endTime, String visibility, String hostname) {
    this.title = title;
    this.description = description;
    this.hostname = hostname;
    this.latitude = latitude;
    this.longitude = longitude;
    this.startTime = startTime;
    this.endTime = endTime;
    this.visibility = visibility;
    this.tags = "";
  }

  public Event() {
  }

  public static class Builder {
    private Event tmpEvent;

    public Builder() {
      tmpEvent = new Event();
    }

    public Builder withTitle(String title) {
      tmpEvent.title = title;
      return this;
    }

    public Builder withHostname(String hostname) {
      tmpEvent.hostname = hostname;
      return this;
    }

    public Builder withLocation(LatLng coord) {
      return withLocation(coord.getLatitude(), coord.getLongitude());
    }

    public Builder withLocation(double latitude, double longitude) {
      tmpEvent.latitude = latitude;
      tmpEvent.longitude = longitude;
      return this;
    }

    public Builder withDescription(String description) {
      tmpEvent.description = description;
      return this;
    }

    public Builder withTime(Date startTime, Date endTime) {
      tmpEvent.startTime = startTime;
      tmpEvent.endTime = endTime;

      return this;
    }

    public Builder withVisibility(String visibility) {
      tmpEvent.visibility = visibility;
      return this;
    }

    public Event build() {
      return tmpEvent;
    }
  }

  @PrimaryKey(autoGenerate = true)
  public int eventId;

  @ColumnInfo(name = "Title")
  public String title;

  @ColumnInfo(name = "Description")
  public String description;

  @ColumnInfo(name =  "HostName")
  public String hostname;

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
