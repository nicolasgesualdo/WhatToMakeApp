package com.example.whattomake3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class DisplayAllRecipeActivity extends AppCompatActivity {

    private TextView theRecipeName;
    private TextView theDifficulty;
    private TextView theCookTime;
    private TextView theNumIngredients;
    private TextView theIngredients;
    private TextView theInstructions;
    private ImageView theFoodPic;
    private TextView theCategory;

    private ArrayList<String> fridge;
    private SQLiteDatabase myDatabase;
    private SQLiteDatabase myListDatabase;

    private ArrayList<String> toShoppingCart = new ArrayList<>();
    private Button addToShopList;

    private ArrayList<String> listItems;
    private ListDatabaseHelper dbListHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_recipe);

        FridgeDatabaseHelper dbHelper = new FridgeDatabaseHelper(this);
        myDatabase = dbHelper.getReadableDatabase();

        dbListHelper = new ListDatabaseHelper(this);
        myListDatabase = dbListHelper.getWritableDatabase();
        listItems = dbListHelper.getAllListItems();

        theRecipeName = (TextView) findViewById(R.id.allRecipeName);
        theDifficulty = (TextView) findViewById(R.id.allDifficulty);
        theCookTime = (TextView) findViewById(R.id.allCookTime);
        theNumIngredients = (TextView) findViewById(R.id.allNumIng);
        theIngredients = (TextView)findViewById(R.id.allIngList);
        theInstructions = (TextView) findViewById(R.id.allInstructions);
        theFoodPic = (ImageView) findViewById(R.id.allRecipePic);
        theCategory = (TextView)findViewById(R.id.allCategory);
        addToShopList = (Button)findViewById(R.id.addToShopList);

        //Receive data
        Intent intent = getIntent();
        String name = intent.getExtras().getString("Name");
        int difficulty = intent.getExtras().getInt("Difficulty");
        int cookTime = intent.getExtras().getInt("CookTime");
        int numIngredients = intent.getExtras().getInt("NumIng");
        String ingredients = intent.getExtras().getString("Ingredients");
        int foodImage = intent.getExtras().getInt("Pic");
        String category = intent.getExtras().getString("Category");
        String instructions = intent.getExtras().getString("Instructions");

        Recipe currentRecipe = new Recipe();
        ArrayList<Ingredient> currentIngArray;
        currentRecipe.setIngArrayListDashProblem(ingredients);
        currentIngArray = currentRecipe.getIngredients();

        theRecipeName.setText(name);
        theFoodPic.setImageResource(foodImage);
        theDifficulty.setText("Difficulty: " + difficulty);
        theCookTime.setText("Cook Time: " + cookTime);
        theNumIngredients.setText("Num Ingredients: " + numIngredients);
        theCategory.setText("Category: " + category);
        theInstructions.setText(instructions);

        setTextIngredients(currentIngArray);

        addToShopList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList();
            }
        });

        //initialize variable
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNav.setSelectedItemId(R.id.nav_recipes);

        //select item listener for bottom bar
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private boolean alreadyExists(String name){
        for(int k = 0; k < listItems.size(); k++){
            if(name.equals(listItems.get(k))){
                return true;
            }
        }
        return false;
    }

    private void addToList(){

        for(int i = 0; i < toShoppingCart.size(); i++){
            String temp = toShoppingCart.get(i);

            listItems = dbListHelper.getAllListItems();
            //prevents duplicate ingredients from being added to the shopping list
            if(alreadyExists(temp) == false){
                ContentValues cv = new ContentValues();
                cv.put(ListContract.ListEntry.COLUMN_NAME, temp);
                //ID and timestamp occur automatically
                myListDatabase.insert(ListContract.ListEntry.TABLE_NAME,null, cv);
            }
        }
    }

    private void getAllItems(){
        Cursor cursor = myDatabase.rawQuery("select * from fridgelist", null);
        fridge = new ArrayList<>();
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){//while true
                String temp = cursor.getString(1);
                fridge.add(temp);
            }
        }
    }

    private void setTextIngredients(ArrayList<Ingredient> currArray){
        getAllItems();

        for(int i = 0; i < currArray.size(); i++){
            int check = 0;

            for(int j = 0; j < fridge.size(); j++){
                String ingName = currArray.get(i).getIngName();
                if(i==0 && ingName.equals(fridge.get(j))){
                    Spannable word = new SpannableString(currArray.get(i).toString());
                    word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    theIngredients.setText(word);
                    theIngredients.append("\n");
                    check = 1;
                }
                else if(ingName.equals(fridge.get(j))){
                    Spannable word = new SpannableString(currArray.get(i).toString());
                    word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    theIngredients.append(word);
                    theIngredients.append("\n");
                    check = 1;
                }
            }

            if(check == 0){
                Spannable wordTwo = new SpannableString(currArray.get(i).toString());
                wordTwo.setSpan(new ForegroundColorSpan(Color.RED), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                theIngredients.append(wordTwo);
                theIngredients.append("\n");
                toShoppingCart.add(currArray.get(i).getIngName());
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.nav_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.nav_fridge:
                    startActivity(new Intent(getApplicationContext(), FridgeActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.nav_shoppingList:
                    startActivity(new Intent(getApplicationContext(), ListActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.nav_recipes:
                    startActivity(new Intent(getApplicationContext(), RecipesActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        }
    };
}