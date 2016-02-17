package ru.binarysimple.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by voffka on 07.10.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "FinCalc", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Main.TABLE_NAME +
                " (_id integer PRIMARY KEY AUTOINCREMENT," +
                " comp_id int NOT NULL," +
                " name text NOT NULL," +
                " pos text NOT NULL," +
                " sal int NOT NULL" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Main.TABLE_NAME_C +
                " (_id integer PRIMARY KEY AUTOINCREMENT," +
                 " name text NOT NULL," +
                " test int NOT NULL" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Main.TABLE_ILL_DAY +
                " (_id integer PRIMARY KEY AUTOINCREMENT," +
                " id_pers integer NOT NULL," +
                " start integer NOT NULL," +
                " end integer NOT NULL" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Main.TABLE_HOLY_DAY +
                " (_id integer PRIMARY KEY AUTOINCREMENT," +
                " id_pers integer NOT NULL," +
                " start integer NOT NULL," +
                " end integer NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}