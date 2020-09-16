package com.yyh.read666.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookSQLiteOpenHelper extends SQLiteOpenHelper {

    public static BookSQLiteOpenHelper mySQLiteOpenHelper;

    private static final String DB_NAME = "video.db";
    private static final int VERSION = 1;

    private final static String TABLE_NAME = "video_table";
    public final static String FIELD_id = "_id";
    public final static String FIELD_BOOK_ID = "book_id";
    public final static String FIELD_VIDEO_TIME = "video_time";
    public final static String FIELD_AUDIO_TIME = "audio_time";

    public synchronized static BookSQLiteOpenHelper getInstance(Context context) {
        if (null == mySQLiteOpenHelper) {
            mySQLiteOpenHelper = new BookSQLiteOpenHelper(context, DB_NAME, null, VERSION);
        }
        return mySQLiteOpenHelper;
    }

    /**
     * 当开发者调用 getReadableDatabase(); 或者 getWritableDatabase();
     * 就会通过此构造方法配置的信息 来创建 person_info.db 数据库
     * 此方法的另外作用是，如果存着数据库就打开数据库，不存着数据库就创建数据库
     * @param context 上下文
     * @param name    数据库名
     * @param factory 游标工厂
     * @param version 版本，最低为1
     */
    private BookSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 此方法是何时调用? ，是需要开发者调用 getReadableDatabase(); 或者 getWritableDatabase();
     * 此方法的作用是，如果没有表就创建打开，如果有表就打开
     * @param db 可执行SQL语句
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + FIELD_id + " INTEGER primary key autoincrement, " + " " + FIELD_BOOK_ID + " text, "+ " " + FIELD_VIDEO_TIME +" INTEGER ,"+FIELD_AUDIO_TIME+" INTEGER"+           ")";
        db.execSQL(sql);

//        db.execSQL("create table midea_table(_id integer primary key autoincrement, name text, age integer);");
    }

    /**
     * 此方法用于数据库升级
     * @param db 可执行SQL语句
     * @param oldVersion 以前旧版本的版本号
     * @param newVersion 现在目前最新的版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 返回记忆播放的时间
     * @param bookId
     * @return
     */
    public int selectVideoTime(String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, FIELD_BOOK_ID +"=? ", new String[]{bookId}, null, null, null);
        if (cursor.getCount()!=0){
            cursor.moveToFirst();
            int time=cursor.getInt(cursor.getColumnIndex(FIELD_VIDEO_TIME));
            cursor.close();
            return time;
        }else {
            return -1;
        }
    }


    public long insertVideoTime(String videoId,int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        /* 将新增的值放入ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(FIELD_BOOK_ID, videoId);
        cv.put(FIELD_VIDEO_TIME, time);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }


    public void updateVideoTime(String videoId, int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_BOOK_ID + " = ?";
        String[] whereValue = {videoId };
        /* 将修改的值放入ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(FIELD_VIDEO_TIME, time);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    public void updateVideoTime2(String bookId, int time){
        if (selectVideoTime(bookId)!=-1){
            updateVideoTime(bookId,time);
        }else {
            insertVideoTime(bookId,time);
        }
    }


    /**
     * 返回记忆播放的音频时间
     * @param bookId
     * @return
     */
    public int selectAudioTime(String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, FIELD_BOOK_ID +"=? ", new String[]{bookId}, null, null, null);
        System.out.println("cursor:"+cursor.getCount());
        if (cursor.getCount()!=0){
            cursor.moveToFirst();
            int time=cursor.getInt(cursor.getColumnIndex(FIELD_AUDIO_TIME));
            cursor.close();
            return time;
        }else {
            return -1;
        }
    }


    public long insertAudioTime(String bookId,int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        /* 将新增的值放入ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(FIELD_BOOK_ID, bookId);
        cv.put(FIELD_AUDIO_TIME, time);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }


    public void updateAudioTime(String bookid, int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_BOOK_ID + " = ?";
        String[] whereValue = {bookid };
        /* 将修改的值放入ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(FIELD_AUDIO_TIME, time);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    public void updateAudioTime2(String bookId, int time){
        if (selectAudioTime(bookId)!=-1){
            updateAudioTime(bookId,time);
        }else {
            insertAudioTime(bookId,time);
        }
    }

}