package com.example.alanrgan.illinihub;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class Tag {

  @PrimaryKey @NonNull
  public String tagName;

  public Tag(String tagName) {
    this.tagName =tagName;
  }
}
