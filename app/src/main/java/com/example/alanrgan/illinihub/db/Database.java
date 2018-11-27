package com.example.alanrgan.illinihub.db;


import androidx.room.RoomDatabase;
import androidx.room.Room;
import androidx.room.TypeConverters;
import android.content.Context;

import com.example.alanrgan.illinihub.Event;
import com.example.alanrgan.illinihub.EventTagJoin;
import com.example.alanrgan.illinihub.Tag;

@androidx.room.Database(entities = {Event.class, Tag.class, EventTagJoin.class}, version = 6)
@TypeConverters({DateTypeConverter.class})
public abstract class Database extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract TagDao tagDao();
    public abstract EventTagJoinDao eventTagJoinDao();
    private static volatile Database INSTANCE;

    static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "event").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
