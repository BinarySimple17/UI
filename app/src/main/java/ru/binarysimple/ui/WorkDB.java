package ru.binarysimple.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



/**
 * Created by voffka on 11.10.2015.
 */
public class WorkDB {
    final String LOG_TAG = "fc_log";
    DBHelper dbHelper;

    public long insertRecord (Context context, String tableName, ContentValues cv){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Insert record into " + tableName);
        long rowID = db.insert(tableName, null, cv); // insert new record to db and return its rowId
        dbHelper.close();
        return rowID;
    }

    public void updateRecord (Context context, String tableName, ContentValues cv, String id){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Update record in "+tableName);
        db.update(tableName, cv, "_id = ?", new String[]{id});
        dbHelper.close();
    }

    public Cursor getData(Context context, String query, String[] args){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
            return db.rawQuery(query, args);
    }

    public void delPersons(Context context, String c_id){
        Log.d(LOG_TAG, "delete persons");
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM info WHERE comp_id="+c_id);
        dbHelper.close();
    }

    public void delData(Context context, String tableName, String id){
        Log.d(LOG_TAG, "delete data from "+tableName);
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM "+tableName+" WHERE _id="+id);
        dbHelper.close();

    }

}
