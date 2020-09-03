package com.example.whattomake3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class FridgeDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "fridgelist.db";
    public static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public FridgeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //_ID was given from base class calling super
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_FRIDGELIST_TABLE = "CREATE TABLE " +
            FridgeContract.FridgeEntry.TABLE_NAME + " (" +
            FridgeContract.FridgeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FridgeContract.FridgeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            FridgeContract.FridgeEntry.COLUMN_TIMESTAMP + "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ");";

        db.execSQL(SQL_CREATE_FRIDGELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FridgeContract.FridgeEntry.TABLE_NAME);
        onCreate(db);
    }
    //new
    public ArrayList<String> getAllFridgeItems(){
        ArrayList<String> fridgeArrayList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from fridgelist",null);

        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){//while true
                String ing = cursor.getString(cursor.getColumnIndex(FridgeContract.FridgeEntry.COLUMN_NAME));
                fridgeArrayList.add(ing);
            }
        }
        cursor.close();
        return fridgeArrayList;
    }
}
