package com.example.nt17;

import android.content.Context;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {list_ghichu.class},version = 1,exportSchema = false)
public abstract class database extends RoomDatabase {
    public abstract DaoAccess daoAccess();

    private static database Instance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static database getDatabase (Context context,String name) {
        Instance= Room.databaseBuilder(context,database.class,name).allowMainThreadQueries().build();
        return Instance;
    }



}

