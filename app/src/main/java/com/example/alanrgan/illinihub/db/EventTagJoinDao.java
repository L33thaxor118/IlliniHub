package com.example.alanrgan.illinihub.db;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;

import com.example.alanrgan.illinihub.Event;
import com.example.alanrgan.illinihub.EventTagJoin;

import java.util.List;
@Dao
public interface EventTagJoinDao {
  @Insert
  void insert(EventTagJoin eventTagJoin);

  @Query("SELECT * FROM eventtagjoin")
  List<EventTagJoin> getAll();

  @Query("DELETE FROM eventtagjoin")
  void deleteAll();

  @RawQuery
  List<Event> getEventsByTag(SupportSQLiteQuery query);

  @Query("SELECT t.tagName FROM event e JOIN eventtagjoin et1 ON " +
    "et1.event_id = e.eventId JOIN tag t ON t.tagName = et1.tag_name " +
    "WHERE e.eventId = :id")
  List<String> getTagsForEvent(int id);
}
