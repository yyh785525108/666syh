package com.yyh.read666.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AudioSQLiteOpenHelper extends SQLiteOpenHelper {

    public static AudioSQLiteOpenHelper mySQLiteOpenHelper;

    private static final String DB_NAME = "audio.db";
    private static final int VERSION = 1;

    private final static String TABLE_NAME = "audio_table";
    public final static String FIELD_id = "_id";
    public final static String FIELD_BOOK_ID = "book_id";
    public final static String FIELD_ALBUM_ID = "album_id";
    public final static String FIELD_AUDIO_TIME = "audio_time";

    public synchronized static AudioSQLiteOpenHelper getInstance(Context context) {
        if (null == mySQLiteOpenHelper) {
            mySQLiteOpenHelper = new AudioSQLiteOpenHelper(context, DB_NAME, null, VERSION);
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
    private AudioSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 此方法是何时调用? ，是需要开发者调用 getReadableDatabase(); 或者 getWritableDatabase();
     * 此方法的作用是，如果没有表就创建打开，如果有表就打开
     * @param db 可执行SQL语句
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + FIELD_id + " INTEGER primary key autoincrement, " + " " + FIELD_BOOK_ID + " text, "+ " " + FIELD_ALBUM_ID +" TEXT ,"+FIELD_AUDIO_TIME+" INTEGER"+           ")";
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
     * 返回记忆播放的音频时间
     * @param bookId
     * @return
     */
    public int selectAudioTime(String bookId,String albumId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, FIELD_BOOK_ID +"=? AND "+FIELD_ALBUM_ID+"=?", new String[]{bookId,albumId}, null, null, null);
        if (cursor.getCount()!=0){
            cursor.moveToFirst();
            System.out.println(FIELD_BOOK_ID+":"+bookId+"  "+FIELD_ALBUM_ID+":"+albumId);
            int time=cursor.getInt(cursor.getColumnIndex(FIELD_AUDIO_TIME));
            System.out.println("time:"+time);
            cursor.close();
            return time;
        }else {
            return -1;
        }
    }


    public long insertAudioTime(String bookId,String albumId,int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        /* 将新增的值放入ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(FIELD_BOOK_ID, bookId);
        cv.put(FIELD_ALBUM_ID, albumId);

        cv.put(FIELD_AUDIO_TIME, time);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }


    public void updateAudioTime(String bookid,String albumId, int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_BOOK_ID + " = ? AND "+FIELD_ALBUM_ID + " = ?";
        String[] whereValue = {bookid,albumId };
        /* 将修改的值放入ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(FIELD_AUDIO_TIME, time);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    public void updateAudioTime2(String bookId,String albumId, int time){
        if (selectAudioTime(bookId,albumId)!=-1){
            updateAudioTime(bookId,albumId,time);
        }else {
            insertAudioTime(bookId,albumId,time);
        }
    }

}