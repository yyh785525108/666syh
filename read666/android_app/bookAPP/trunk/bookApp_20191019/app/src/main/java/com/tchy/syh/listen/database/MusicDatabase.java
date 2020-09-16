package com.tchy.syh.listen.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;


@Database(entities = {AudioHistory.class, AudioPlayProgress.class, AlbumPlayProgress.class}, version = 2, exportSchema = false)
public abstract class MusicDatabase extends RoomDatabase {

    private static final String DB_NAME = "syh_music.db";
    private static volatile MusicDatabase instance;

    public static synchronized MusicDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static MusicDatabase create(Context context) {
        return Room.databaseBuilder(
                context,
                MusicDatabase.class,
                DB_NAME).allowMainThreadQueries()//允许在主线程查询数据
                .addMigrations()//迁移数据库使用
                .fallbackToDestructiveMigration()//迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                .build();

    }

    public abstract AudioHistoryDao audioHistoryDao();

    public abstract AudioPlayProgressDao audioPlayProgressDao();

    public abstract AlbumPlayProgressDao albumPlayProgressDao();
}

