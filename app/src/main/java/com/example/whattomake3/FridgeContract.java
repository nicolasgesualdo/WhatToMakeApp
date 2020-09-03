package com.example.whattomake3;

import android.provider.BaseColumns;

public class FridgeContract {

    private FridgeContract(){}

    //inner class for each table
    public static final class FridgeEntry implements BaseColumns{
        public static final String TABLE_NAME = "fridgeList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIMESTAMP = "timeStamp";
    }
}
