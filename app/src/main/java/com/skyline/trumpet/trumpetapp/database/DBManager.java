package com.skyline.trumpet.trumpetapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skyline.trumpet.trumpetapp.model.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/17.
 */
public class DBManager {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBManager(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addTags(List<Tag> tags){
        db.beginTransaction(); //start transaction
        try {
            for (Tag tag : tags)
                db.execSQL("INSERT INTO Tag VALUES(null,?,?)", new Object[]{tag.getTag_name(), tag.getTag_count()});
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void add(Tag tag){
        db.execSQL("INSERT INTO Tag VALUES(null,?,?)",new Object[]{tag.getTag_name(),tag.getTag_count()});
    }

    public List<Tag> selectAll(){
        List<Tag> tags = new ArrayList<Tag>();
        Cursor cursor = db.rawQuery("SELECT * from Tag",null);
        while(cursor.moveToNext()){
            Tag tag = new Tag();
            tag.setTag_name(cursor.getString(cursor.getColumnIndex("tag_name")));
            tag.setTag_count(cursor.getInt(cursor.getColumnIndex("tag_count")));
            tags.add(tag);
        }
        cursor.close();
        return tags;

    }

    public void removeAll(){
        db.delete("Tag",null,null);
    }

    public void closeDB(){
        db.close();
    }
}
