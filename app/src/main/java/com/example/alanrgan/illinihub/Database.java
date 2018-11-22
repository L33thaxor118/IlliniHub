package com.example.alanrgan.illinihub;


import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@android.arch.persistence.room.Database(entities = {Event.class},version = 1)
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
