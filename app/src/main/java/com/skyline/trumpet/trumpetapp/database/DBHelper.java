package com.skyline.trumpet.trumpetapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/7/17.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trumpet.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //will be called only on the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Tag(tag_id INTEGER PRIMARY KEY AUTOINCREMENT, tag_name VARCHAR, tag_count INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE Tag ADD COLUMN other STRING");
    }
}
