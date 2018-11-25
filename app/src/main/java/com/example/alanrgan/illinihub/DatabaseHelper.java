package com.example.alanrgan.illinihub;

import android.content.Context;
import android.os.AsyncTask;

import com.example.alanrgan.illinihub.util.DBHelperAsyncResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that encapsulates the database and provides helper functions for easy queries.
 */
public class DatabaseHelper {
  private Database db;

  public DatabaseHelper(Context context) {
    db = Database.getDatabase(context);
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
    for (int i = 0; i < 10; i++) {
      Event e = new Event();
      e.longitude = -88.227 + (float) i / 10000;
      e.latitude = 40.107;
      e.title = "sample " + String.valueOf(i);
      e.description = "this is a sample event";
      ArrayList<String> tags = new ArrayList<String>();
      if ((i % 2) == 0) tags.add("Business");
      tags.add("21+");
      tags.add("Food");
      tags.add("Free");
      tags.add("GiveAway");
      String tagsJson = new Gson().toJson(tags);
      e.tags = tagsJson;
      System.out.println("Tags are " + tagsJson);
      new DBInsertAsyncTask(db.eventDao()).execute(e);
    }
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
      System.out.print("Params: ");
      for (String param : params) {
        System.out.printf("%s, ", param);
      }
      System.out.println();
//      System.out.println("Params are " + params.toString());
      return dao.getMatches(params[0]);
    }

    @Override
    protected void onPostExecute(List<Event> result) {
      delegate.processFinishGetMatches(result);
    }
  }

}
