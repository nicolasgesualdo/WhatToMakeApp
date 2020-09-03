package com.example.whattomake3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Collections;

public class RecipeDisplayActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipe;
    private ArrayList<String> fridge;
    private ArrayList<String> shoppingList;
    private ArrayList<Recipe> makeableList = new ArrayList<>();

    private int currentRecipeIndex = 0;
    private String currentEntityName = "";

    private TextView theRecipeName;
    private TextView theDifficulty;
    private TextView theCookTime;
    private TextView theNumIngredients;
    private TextView theIngredientList;
    private ImageView theFoodPic;
    private Button theNextButton;
    private Button theBackButton;
    private TextView theCategory;
    private TextView theInstructions;

    private SQLiteDatabase myDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

        theRecipeName = (TextView)findViewById(R.id.recipeName);
        theDifficulty = (TextView)findViewById(R.id.difficulty);
        theCookTime = (TextView)findViewById(R.id.cookingTime);
        theNumIngredients = (TextView)findViewById(R.id.numIngredients);
        theIngredientList = (TextView)findViewById(R.id.ingredientList);
        theFoodPic = (ImageView)findViewById(R.id.foodPic);
        theNextButton = (Button)findViewById(R.id.nextButton);
        theBackButton = (Button)findViewById(R.id.backButton);
        theCategory = (TextView)findViewById(R.id.categoryARD);
        theInstructions = (TextView)findViewById(R.id.instructionsARD);

        FridgeDatabaseHelper dbHelper = new FridgeDatabaseHelper(this);
        myDatabase = dbHelper.getReadableDatabase();

        RecipeDatabaseHelper dbHelperRecipe = new RecipeDatabaseHelper(this);
        recipe = dbHelperRecipe.getAllRecipes();

        getAllItems();

        fridge.add("Water");//assuming every kitchen has water

        makeableList.addAll(makeable());

        displayRecipe(0);

        //initialize variable
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNav.setSelectedItemId(R.id.nav_home);

        //select item listener for bottom bar
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        theNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentRecipeIndex < (makeableList.size()-1)) {
                    displayRecipe(++currentRecipeIndex);
                }
            }
        });
        theBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentRecipeIndex>0){
                    displayRecipe(--currentRecipeIndex);
                }
            }
        });
    }

    private void getAllItems(){
       Cursor cursor = myDatabase.rawQuery("select * from fridgelist", null);

       if(cursor.getCount()!=0){
           while(cursor.moveToNext()){//while true
               String temp = cursor.getString(1);
               fridge.add(temp);
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

    public void displayRecipe(int index)
    {
        if(makeableList.size() == 0)
        {
            theRecipeName.setText("ERROR");
            Toast.makeText(RecipeDisplayActivity.this, "Add items to fridge", Toast.LENGTH_LONG).show();
        }
        else
        {
            Recipe theRecipe = makeableList.get(index);
            currentEntityName = theRecipe.getName();
            theRecipeName.setText(theRecipe.getName());
            theDifficulty.setText("Difficulty: " + theRecipe.getDifficulty());
            theCookTime.setText("Cooking Time: " + theRecipe.getCookTime());
            theNumIngredients.setText(("Num Ingredients: "  + theRecipe.getNumIngredients()));
            theIngredientList.setText(theRecipe.printArrayList());
            theCategory.setText(("Category: " + theRecipe.getCategory()));
            theInstructions.setText(theRecipe.getInstructions());

            theFoodPic.setImageResource(theRecipe.getImage());
        }
    }

    public RecipeDisplayActivity()
    {
        fridge = new ArrayList<String>();
        recipe = new ArrayList<Recipe>();
        shoppingList = new ArrayList<String>();
    }

    public ArrayList<Recipe> makeable()
    {
        ArrayList<Recipe> both = new ArrayList<>();

        for(int i = 0; i < recipe.size(); i++)
        {
            boolean firstIngNotInFridge = false;
            int matchCount = 0;
            Recipe temp = recipe.get(i);
            ArrayList<Ingredient> tempList = temp.getIngredients();
            for(int j = 0; j < tempList.size(); j++)
            {
                for(int k = 0; k < fridge.size();k++)
                {
                    if((tempList.get(j).getIngName()).equals(fridge.get(k)))//new version with getIngName()
                    {
                        matchCount++;
                        break;
                    }
                    else if(k == (fridge.size() - 1))
                    {
                        firstIngNotInFridge = true;
                    }
                }

                if(firstIngNotInFridge == true)
                    break;

            }
            if(matchCount == temp.getNumIngredients())
            {
                both.add(temp);
            }
        }
        return both;
    }

    public void difficultySort()//Bubble Sort
    {
        for(int i = 0; i < recipe.size()-1; i++)
        {
            for(int j = i; j < recipe.size()-i-1; j++)
            {
                Recipe current = new Recipe(recipe.get(j));
                Recipe next = new Recipe(recipe.get(j+1));
                if(current.getDifficulty() > next.getDifficulty())
                {
                    Collections.swap(recipe, j, j+1);
                }
            }
        }
    }

    public void timeSort()//Bubble Sort
    {
        for(int i = 0; i < recipe.size()-1; i++)
        {
            for(int j = i; j < recipe.size()-i-1; j++)
            {
                Recipe current = new Recipe(recipe.get(j));
                Recipe next = new Recipe(recipe.get(j+1));
                if(current.getCookTime() > next.getCookTime())
                {
                    Collections.swap(recipe, j, j+1);
                }
            }
        }
    }

    public void numIngredientSort()//Bubble Sort
    {
        for(int i = 0; i < recipe.size()-1; i++)
        {
            for(int j = i; j < recipe.size()-i-1; j++)
            {
                Recipe current = new Recipe(recipe.get(j));
                Recipe next = new Recipe(recipe.get(j+1));
                if(current.getNumIngredients() > next.getNumIngredients())
                {
                    Collections.swap(recipe, j, j+1);
                }
            }
        }
    }
}
