package com.example.hoopy.database;


import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hoopy.models.User;

@androidx.room.Database(
        entities = {User.class},
        version = 1,
        exportSchema = true
)
public abstract class Database extends RoomDatabase {

    public abstract Repo Dao();

    private static Database database = null;

    public static Database getDatabase(final Context context) {

        if (database == null) {
            synchronized (Database.class) {
                if (database == null) {

                    database = Room.databaseBuilder(context.getApplicationContext(), Database.class, "news.db")
                            .build();

                }
            }
        }

        return database;
    }


}
