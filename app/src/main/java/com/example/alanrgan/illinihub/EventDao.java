package com.example.alanrgan.illinihub;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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

