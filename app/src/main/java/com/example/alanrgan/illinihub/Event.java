package com.example.alanrgan.illinihub;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
}
