package com.example.alanrgan.illinihub.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.alanrgan.illinihub.Event;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface EventDao {
  @Query("SELECT * FROM event")
  List<Event> getAll();

  @Query("SELECT * FROM event WHERE `tags(json)` LIKE :search ")
  List<Event> getMatches(String search);

  @Insert
  long insert(Event event);

  @Insert
  long[] insertAll(Event... events);

  @Query("UPDATE event SET thumbsCt = :count WHERE eventId = :id")
  void setThumbCount(int id, int count);

  @Delete
  void delete(Event event);

  @Query("DELETE FROM event")
  void deleteAll();

}

