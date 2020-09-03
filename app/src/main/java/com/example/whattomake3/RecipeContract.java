package com.example.whattomake3;

import android.provider.BaseColumns;

public class RecipeContract {
    private RecipeContract(){}

    //inner class for each table
    public static final class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipeList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_COOKTIME = "cookTime";
        public static final String COLUMN_NUMINGREDIENTS = "numIngredients";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_INSTRUCTIONS = "instructions";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_CATEGORY = "category";
    }
}
