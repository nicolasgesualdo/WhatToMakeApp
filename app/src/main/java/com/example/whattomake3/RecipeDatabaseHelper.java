package com.example.whattomake3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RecipeDatabaseHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipelist.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //_ID was given from base class calling super
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_RECIPELIST_TABLE = "CREATE TABLE " +
                RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_DIFFICULTY + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_COOKTIME + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_NUMINGREDIENTS + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_INSTRUCTIONS + " TEXT NOT NULL," +
                RecipeContract.RecipeEntry.COLUMN_IMAGE + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_CATEGORY + " TEXT NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_RECIPELIST_TABLE);
        fillRecipesTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        onCreate(db);
    }

    private void fillRecipesTable(){
        ArrayList<Ingredient> ingPC = new ArrayList<>();
        ingPC.add(new Ingredient("Flour", "1 cup"));
        ingPC.add(new Ingredient("Baking Powder", "2 tbsp"));
        ingPC.add(new Ingredient("Salt", "2 pinches"));
        ingPC.add(new Ingredient("Eggs", "1"));
        ingPC.add(new Ingredient("Milk", "1 1/4 cup"));
        ingPC.add(new Ingredient("Butter", "1 tbsp"));
        ingPC.add(new Ingredient("Sugar", "2 tbsp"));
        Recipe pancakes = new Recipe("Pancakes", 2, 30,
                7, ingPC, "In one medium sized bowl combine the dry ingredients including" +
                " flour, baking powder, sugar and salt. In a large bowl crack and beat the egg, then add milk. " +
                "Next melt butter and add it to the large bowl. Pour dry ingredients into the large bowl and whisk until fully mixed. " +
                "Place 1/4 cup of pancake mix into a pan on medium-high heat. Flip once golden brown. ",R.drawable.pancakeimageee,"Breakfast");
        addRecipe(pancakes);

        ArrayList<Ingredient> ingPBnJ = new ArrayList<>();
        ingPBnJ.add(new Ingredient("Slice Bread", "2 slices"));
        ingPBnJ.add(new Ingredient("Peanut Butter", ""));
        ingPBnJ.add(new Ingredient("Jam", ""));
        Recipe PBnJ = new Recipe("PBnJ", 1, 2, 3, ingPBnJ,
                "Spread a layer of peanut butter on the first slice of bread. " +
                "Spread a layer of jam on the second slice of bread. Place to slices of bread together allowing toppings to touch. ",
                R.drawable.pbnj, "Breakfast");
        addRecipe(PBnJ);

        ArrayList<Ingredient> ingOP = new ArrayList<>();
        ingOP.add(new Ingredient("Pasta", "1 cup"));
        ingOP.add(new Ingredient("Olive Oil", "1/4 cup"));
        ingOP.add(new Ingredient("Garlic", "2 cloves"));
        Recipe olivePasta = new Recipe("Pasta with Olive Oil", 2, 30, 3, ingOP,
                "Heat up water in large pot, and boil. Once boiling place desired amount of pasta in pot and cook for 12 minutes. " +
                        "Dice 2 cloves of garlic and pour into a small pot with olive oil. Heat olive oil until garlic is golden then pour over pasta",
                R.drawable.pastaoliveoil, "Italian");
        addRecipe(olivePasta);

        ArrayList<Ingredient> ingTJ = new ArrayList<>();
        ingTJ.add(new Ingredient("Slice Bread", "2 slices"));
        ingTJ.add(new Ingredient("Jam", ""));
        Recipe jamToast = new Recipe("Toast with Jam", 1, 1, 2, ingTJ,
                "Spread your desired amount of jam on slice bread", R.drawable.jamtoast, "Breakfast");
        addRecipe(jamToast);

        ArrayList<Ingredient> ingBeefStew = new ArrayList<>();
        ingBeefStew.add(new Ingredient("Beef Stew Seasoning", "1 pack"));
        ingBeefStew.add(new Ingredient("Beef", "2 lbs"));
        ingBeefStew.add(new Ingredient("Flour", "1/4 cup"));
        ingBeefStew.add(new Ingredient("Potato", "2 cups"));
        ingBeefStew.add(new Ingredient("Carrot", "1 1/4 cups"));
        ingBeefStew.add(new Ingredient("Onion", "1"));
        Recipe beefStew = new Recipe("Beef Stew", 3, 60, 6, ingBeefStew,
                "Cut beef into 1 inch cubes and coat in flour. Pour oil in a large pot and sear beef until brown on the outside. " +
                        "Place rest of ingredients into pot and stir. Bring to a boil and cook for 30 minutes. " +
                        "Then reduce heat to low and simmer for 15 minutes.",R.drawable.beefstewimage, "American");
        addRecipe(beefStew);

        ArrayList<Ingredient> ingBuffaloWings = new ArrayList<>();
        ingBuffaloWings.add(new Ingredient("Chicken Wings", "12"));
        ingBuffaloWings.add(new Ingredient("Buffalo Sauce", "6 tbsp"));
        Recipe buffaloWings = new Recipe("Buffalo Wings", 2, 35, 2, ingBuffaloWings,
                "Cook wings in the oven for 30 minutes at 450 degrees fahrenheit. " +
                        "Place wings in a medium sized bowl and pour buffalo sauce on wings. " +
                        "Toss wings coating them evenly in buffalo sauce.", R.drawable.buffalowingsimage, "American");
        addRecipe(buffaloWings);

        ArrayList<Ingredient> ingBBQWings = new ArrayList<>();
        ingBBQWings.add(new Ingredient("Chicken Wings", "12"));
        ingBBQWings.add(new Ingredient("BBQ Sauce", "6 tbsp"));
        Recipe bbqWings = new Recipe("BBQ Wings", 2, 35, 2, ingBBQWings,
                "Cook wings in the oven for 30 minutes at 450 degrees fahrenheit. " +
                "Place wings in a medium sized bowl and pour BBQ sauce on wings. " +
                "Toss wings coating them evenly in BBQ sauce.", R.drawable.bbqwingsimage, "American");
        addRecipe(bbqWings);
    }

    private void addRecipe(Recipe recipe){
        ContentValues cv = new ContentValues();
        cv.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipe.getName());
        cv.put(RecipeContract.RecipeEntry.COLUMN_DIFFICULTY, recipe.getDifficulty());
        cv.put(RecipeContract.RecipeEntry.COLUMN_COOKTIME, recipe.getCookTime());
        cv.put(RecipeContract.RecipeEntry.COLUMN_NUMINGREDIENTS, recipe.getNumIngredients());
        cv.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS, recipe.printArrayList());
        cv.put(RecipeContract.RecipeEntry.COLUMN_INSTRUCTIONS, recipe.getInstructions());
        cv.put(RecipeContract.RecipeEntry.COLUMN_IMAGE, recipe.getImage());
        cv.put(RecipeContract.RecipeEntry.COLUMN_CATEGORY, recipe.getCategory());
        db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null,cv);
    }

    public ArrayList<Recipe> getAllRecipes(){
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from recipelist",null);

        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){//while true
               Recipe recipe = new Recipe();
               recipe.setName(cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME)));
               recipe.setDifficulty(cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_DIFFICULTY)));
               recipe.setCookTime(cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_COOKTIME)));
               recipe.setNumIngredients(cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NUMINGREDIENTS)));
               recipe.setIngArrayList(cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS)));
               recipe.setInstructions(cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INSTRUCTIONS)));
               recipe.setImage(cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE)));
               recipe.setCategory(cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_CATEGORY)));
               recipeArrayList.add(recipe);
            }
        }
        cursor.close();
        return recipeArrayList;
    }
}

