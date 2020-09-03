package com.example.whattomake3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class ListDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "shoppinglist.db";
    public static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public ListDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //_ID was given from base class calling super
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_SHOPPINGLIST_TABLE = "CREATE TABLE " +
                ListContract.ListEntry.TABLE_NAME + " (" +
                ListContract.ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ListContract.ListEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ListContract.ListEntry.COLUMN_TIMESTAMP + "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_SHOPPINGLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ListContract.ListEntry.TABLE_NAME);
        onCreate(db);
    }

    //new
    public ArrayList<String> getAllListItems(){
        ArrayList<String> listArrayList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shoppinglist",null);

        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){//while true
                String ing = cursor.getString(cursor.getColumnIndex(ListContract.ListEntry.COLUMN_NAME));
                listArrayList.add(ing);
            }
        }
        cursor.close();
        return listArrayList;
    }
}
