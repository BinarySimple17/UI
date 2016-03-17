package ru.binarysimple.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {
        // Use application context
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBHelper(Context context) {
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
                " test int NOT NULL," +
                " ndfl text," +
                " pfr text," +
                " fss text," +
                " ffoms text," +
                " year text," +
                " month int" +
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
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Main.TABLE_RESULTS +
                " (_id integer PRIMARY KEY AUTOINCREMENT," +
                " id_person integer NOT NULL," +
                " month integer NOT NULL," +
                " year integer NOT NULL," +
                " ndfl text NOT NULL," +
                " ffoms text NOT NULL," +
                " pfr text NOT NULL," +
                " fss text NOT NULL," +
                " name text NOT NULL," +
                " position text NOT NULL," +
                " salary text NOT NULL," +
                " comp_id text NOT NULL" +
                ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}