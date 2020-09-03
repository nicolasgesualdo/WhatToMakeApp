package com.example.whattomake3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CustomRecipeActivity extends AppCompatActivity {

    private EditText addDifficulty;
    private EditText addName;
    private EditText addCookTime;
    private EditText addIng;
    private EditText addQuantity;
    private EditText addInstructions;
    private Button add;
    private Button submit;
    private EditText addCategory;

    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private CustomAdapter myAdapter;
    private SQLiteDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recipe);

        RecipeDatabaseHelper dbHelper = new RecipeDatabaseHelper(this);
        myDatabase = dbHelper.getWritableDatabase();

        addDifficulty = findViewById(R.id.addRecipeDifficulty);
        addName = findViewById(R.id.addRecipeName);
        addCookTime = findViewById(R.id.addCookingTime);
        addIng = findViewById(R.id.enterIngName);
        addQuantity = findViewById(R.id.enterIngQuantity);
        addInstructions = findViewById(R.id.addInstructions);
        add = findViewById(R.id.enterIngAdd);
        submit = findViewById(R.id.submitRecipe);
        addCategory = findViewById(R.id.enterCategory);

        //build RecyclerView
        RecyclerView theRecyclerView = findViewById(R.id.customRecyclerView);
        theRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new CustomAdapter(ingredientList);
        theRecyclerView.setAdapter(myAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCurrentIngredient();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTheRecipe();
                Toast.makeText(CustomRecipeActivity.this,"Recipe Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        myAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int id) {
                ingredientList.remove(id);
                myAdapter.notifyItemRemoved(id);
            }
        });

        //initialize variable
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNav.setSelectedItemId(R.id.nav_home);

        //select item listener for bottom bar
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private void addCurrentIngredient(){
        Ingredient ingredient = new Ingredient();
        ingredient.setIngName(addIng.getText().toString());
        addIng.getText().clear();
        ingredient.setQuantity(addQuantity.getText().toString());
        addQuantity.getText().clear();

        ingredientList.add(ingredient);
        myAdapter.notifyItemInserted(ingredientList.size()-1);
    }

    private void submitTheRecipe(){
        String name = addName.getText().toString();
        addName.getText().clear();
        int difficulty = Integer.parseInt(addDifficulty.getText().toString());
        addDifficulty.getText().clear();
        int cookTime = Integer.parseInt(addCookTime.getText().toString());
        addCookTime.getText().clear();
        int numIngredients = ingredientList.size();
        String theIngredients = printTheArrayList(ingredientList);
        String instructions = addInstructions.getText().toString();
        addInstructions.getText().clear();
        int image = R.drawable.customrecipepic;
        String category = addCategory.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(RecipeContract.RecipeEntry.COLUMN_NAME, name);
        cv.put(RecipeContract.RecipeEntry.COLUMN_DIFFICULTY, difficulty);
        cv.put(RecipeContract.RecipeEntry.COLUMN_COOKTIME, cookTime);
        cv.put(RecipeContract.RecipeEntry.COLUMN_NUMINGREDIENTS, numIngredients);
        cv.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS, theIngredients);
        cv.put(RecipeContract.RecipeEntry.COLUMN_INSTRUCTIONS, instructions);
        cv.put(RecipeContract.RecipeEntry.COLUMN_IMAGE, image);
        cv.put(RecipeContract.RecipeEntry.COLUMN_CATEGORY, category);
        myDatabase.insert(RecipeContract.RecipeEntry.TABLE_NAME,null, cv);

        ingredientList.removeAll(ingredientList);
        myAdapter.notifyDataSetChanged();
    }


    //creates a string containing the entire arraylist
    private String printTheArrayList(ArrayList<Ingredient> ing)
    {
        String temp = "";
        for(Ingredient entry : ing)
        {
            temp += (entry.toString() + "\n");
        }
        return temp;
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
