package com.example.alanrgan.illinihub.db;

import androidx.sqlite.db.SimpleSQLiteQuery;
import android.content.Context;
import android.os.AsyncTask;

import com.example.alanrgan.illinihub.Event;
import com.example.alanrgan.illinihub.EventTagJoin;
import com.example.alanrgan.illinihub.Tag;
import com.example.alanrgan.illinihub.util.DBHelperAsyncResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Class that encapsulates the database and provides helper functions for easy queries.
 */
public class DatabaseHelper {
  private Database db;

  public DatabaseHelper(Context context) {
    db = Database.getDatabase(context);
  }

  public void addEvent(Event event, ArrayList<String> tags){
    int eventId = (int)db.eventDao().insert(event);
    for (String tag : tags){
      EventTagJoin join = new EventTagJoin(eventId, tag);
      db.eventTagJoinDao().insert(join);
    }
  }
  public void addEventAsync(Event event) {
    new DBInsertAsyncTask(db.eventDao()).execute(event);
  }

  public void removeAllEventsAsync() {
    new DBDeleteAsyncTask(db.eventDao()).execute();
  }

  public void deleteEvent(Event event) {
    db.eventDao().delete(event);
  }

  public void getMatchesAsync(String search, DBHelperAsyncResponse delegate) {
    new DBGetMatchesAsyncTask(db.eventDao(), delegate).execute(search);
  }

  public List<Event> getMatches(String search) {
    return db.eventDao().getMatches(search);
  }

  public List<Event> getAll() {
    return db.eventDao().getAll();
  }

  public void populateWithSampleData() {
    db.eventDao().deleteAll();
    db.tagDao().deleteAll();
    db.eventTagJoinDao().deleteAll();
    Tag t1 = new Tag("Business");
    Tag t2 = new Tag("21+");
    Tag t3 = new Tag("Food");
    Tag t4 = new Tag("Free");
    Tag t5 = new Tag("Tech Talk");
    Tag t6 = new Tag("Ladies Night");
    Tag t7 = new Tag("Study Group");
    Tag t8 = new Tag("GiveAway");
    db.tagDao().insertTag(t1);
    db.tagDao().insertTag(t2);
    db.tagDao().insertTag(t3);
    db.tagDao().insertTag(t4);
    db.tagDao().insertTag(t5);
    db.tagDao().insertTag(t6);
    db.tagDao().insertTag(t7);
    db.tagDao().insertTag(t8);

    ArrayList<String> tags = new ArrayList<String>(Arrays.asList("Business", "21+", "Food", "Free", "Tech Talk", "Ladies Night", "Study Group", "GiveAway"));
    for (int i = 0; i < 8; i++) {
      Event e1 = new Event("Sample" + i, "test", 40.107560 + (double)i/10000, -88.228163, new Date(), new Date(),"Public");
      ArrayList<String> current_tags = new ArrayList<String>(tags.subList(0,i+1));
      addEvent(e1, current_tags);
    }
  }
  public List<Event> getMatchingEvents(String query){
    SimpleSQLiteQuery q = new SimpleSQLiteQuery(query);
    return db.eventTagJoinDao().getEventsByTag(q);
  }

  private static class DBInsertAsyncTask extends AsyncTask<Event, Void, Void> {
    private EventDao dao;

    DBInsertAsyncTask(EventDao eventDao) {
      dao = eventDao;
    }

    @Override
    protected Void doInBackground(Event... params) {
      dao.insertAll(params[0]);
      return null;
    }
  }

  private static class DBDeleteAsyncTask extends AsyncTask<Void, Void, Void> {
    private EventDao dao;

    DBDeleteAsyncTask(EventDao eventDao) {
      dao = eventDao;
    }

    @Override
    protected Void doInBackground(final Void... params) {
      dao.deleteAll();
      return null;
    }
  }

  /**
   * This Async task is different from others in that it takes in an interface as a parameter.
   * When this task is done executing, one of the interface functions will be called for further
   * handling. This prevents the main thread from stalling.
   */
  private static class DBGetMatchesAsyncTask extends AsyncTask<String, Void, List<Event>> {
    private EventDao dao;
    public DBHelperAsyncResponse delegate = null;

    DBGetMatchesAsyncTask(EventDao eventDao, DBHelperAsyncResponse deleg) {
      dao = eventDao;
      delegate = deleg;
    }

    @Override
    protected List<Event> doInBackground(String... params) {
      return dao.getMatches(params[0]);
    }

    @Override
    protected void onPostExecute(List<Event> result) {
      delegate.processFinishGetMatches(result);
    }
  }

}
