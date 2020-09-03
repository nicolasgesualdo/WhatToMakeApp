package com.example.whattomake3;

import android.provider.BaseColumns;

public class ListContract {

    private ListContract(){}

    //inner class for each table
    public static final class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "shoppingList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIMESTAMP = "timeStamp";
    }
}

