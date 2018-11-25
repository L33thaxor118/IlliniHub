package com.example.alanrgan.illinihub;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface EventDao {
  @Query("SELECT * FROM event")
  List<Event> getAll();

  @Query("SELECT * FROM event WHERE `tags(json)` LIKE :search ")
  List<Event> getMatches(String search);

  @Insert
  void insertAll(Event... events);

  @Delete
  void delete(Event event);

  @Query("DELETE FROM event")
  void deleteAll();

}

