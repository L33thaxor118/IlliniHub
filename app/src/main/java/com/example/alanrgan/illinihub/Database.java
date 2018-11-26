package com.example.alanrgan.illinihub;


import androidx.room.RoomDatabase;
import androidx.room.Room;
import android.content.Context;

@androidx.room.Database(entities = {Event.class},version = 1)
public abstract class Database extends RoomDatabase {
    public abstract EventDao eventDao();
    private static volatile Database INSTANCE;

    static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "event").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
