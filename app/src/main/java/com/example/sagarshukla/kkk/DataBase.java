package com.example.sagarshukla.kkk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar Shukla on 8/4/2017.
 */

public class DataBase extends SQLiteOpenHelper {
    public DataBase(Context context) {
        super(context, "History", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table Me (Column link)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insert(String link){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Column",link);
        database.insert("Me",null,contentValues);
    }
    public Cursor history(){
        SQLiteDatabase database = getReadableDatabase();
        return database.query("Me",null,null,null,null,null,null);
    }
    public int deleteData(String name) {
        SQLiteDatabase db = getReadableDatabase();
        return db.delete("Me", "Column=?", new String[]{name});
    }
}
