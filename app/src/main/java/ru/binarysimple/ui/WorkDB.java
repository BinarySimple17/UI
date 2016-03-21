package ru.binarysimple.ui;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WorkDB {
    private final String LOG_TAG = "fc_log";
    private DBHelper dbHelper;

    public long insertRecord(Context context, String tableName, ContentValues cv) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Insert record into " + tableName);
        long rowID = db.insert(tableName, null, cv); // insert new record to db and return its rowId
        dbHelper.close();
        return rowID;
    }

    public long insertRecordOnConflict(Context context, String tableName, ContentValues cv) {
        //  dbHelper = new DBHelper(context);
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Insert record into " + tableName);
        //long rowID = db.insert(tableName, null, cv); // insert new record to db and return its rowId
        long rowID = db.insertWithOnConflict(tableName, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        dbHelper.close();
        return rowID;
    }

    public void updateRecord(Context context, String tableName, ContentValues cv, String id) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Update record in " + tableName);
        db.update(tableName, cv, "_id = ?", new String[]{id});
        //db.insertWithOnConflict()
        dbHelper.close();
    }

    public Cursor getData(Context context, String query, String[] args) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.rawQuery(query, args);
    }

    public void delPersons(Context context, String c_id) {
        Log.d(LOG_TAG, "delete persons");
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM info WHERE comp_id=" + c_id);
        dbHelper.close();
    }

    public void delOnePersonByID(Context context, String id) {
        Log.d(LOG_TAG, "delete data from " + Main.TABLE_NAME);
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + Main.TABLE_NAME + " WHERE _id=" + id);
        dbHelper.close();
    }

    public void delResultsByIDPerson(Context context, String id) {
        Log.d(LOG_TAG, "delete data from " + Main.TABLE_RESULTS);
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + Main.TABLE_RESULTS + " WHERE id_person=" + id);
        dbHelper.close();
    }

    public void delResults(Context context, String comp_id, String month, String year) {
        //delete from results where comp_id=1 and month=2 and year=2016
        Log.d(LOG_TAG, "delete data from Results");
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM RESULTS WHERE comp_id=" + comp_id + " and month=" + month + " and year=" + year);
        dbHelper.close();
    }

}
