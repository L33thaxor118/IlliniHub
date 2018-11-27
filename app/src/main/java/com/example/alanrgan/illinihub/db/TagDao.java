package com.example.alanrgan.illinihub.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.alanrgan.illinihub.Tag;

import java.util.List;

@Dao
public interface TagDao {
  @Insert
  public void insertTag(Tag tag);

  @Query("DELETE FROM tag")
  void deleteAll();
}
