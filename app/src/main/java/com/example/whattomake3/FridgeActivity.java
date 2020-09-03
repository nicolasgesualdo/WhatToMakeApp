package com.example.whattomake3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class FridgeActivity extends AppCompatActivity {

    private ArrayList<String> fridgeItems;
    private SQLiteDatabase myDatabase;
    private SQLiteDatabase myRecipeDB;
    private ArrayList<Recipe> temp;
    private FridgeAdapter myAdapter;
    private ArrayList<String> allIngredients = new ArrayList<>();

    private Button fridgeEnter;
    private EditText autocomplete;
    private FridgeDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);

        dbHelper = new FridgeDatabaseHelper(this);
        myDatabase = dbHelper.getWritableDatabase();
        fridgeItems = dbHelper.getAllFridgeItems();

        RecipeDatabaseHelper dbRecipeHelper = new RecipeDatabaseHelper(this);
        myRecipeDB = dbRecipeHelper.getReadableDatabase();
        temp = new ArrayList<>();
        temp = dbRecipeHelper.getAllRecipes();

        //build RecyclerView
        RecyclerView theRecyclerView = findViewById(R.id.recyclerView);
        theRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new FridgeAdapter(this, getAllItems());
        theRecyclerView.setAdapter(myAdapter);

        fridgeEnter = findViewById(R.id.enterList);
        autocomplete = findViewById(R.id.autoCompleteTextViewList);
        //method to add ingredients from recipes to allIngredients
        addAutocompleteList();

        AutoCompleteTextView editText = findViewById(R.id.autoCompleteTextViewList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,allIngredients);
        editText.setAdapter(adapter);

        //initialize variable
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNav.setSelectedItemId(R.id.nav_fridge);

        //select item listener for bottom bar
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        fridgeEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDBItem();
            }
        });

        myAdapter.setOnItemClickListener(new FridgeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(long id) {
                removeItem(id);
            }
        });
    }

    private void addAutocompleteList(){
        for(int i = 0; i < temp.size();i++){
            ArrayList<Ingredient> ingredient = new ArrayList<>();
            ingredient = temp.get(i).getIngredients();
            for(int j = 0; j < ingredient.size(); j++){
                String name = ingredient.get(j).getIngName();
                if(alreadyExists(name) == false){
                    allIngredients.add(name);
                }
            }
        }
    }

    private boolean alreadyExists(String name){
        for(int k = 0; k < allIngredients.size(); k++){
            if(name.equals(allIngredients.get(k))){
                return true;
            }
        }
        return false;//doesn't already exist, add to list
    }

    private boolean alreadyExistsTemp(String name){
        for(int k = 0; k < fridgeItems.size(); k++){
            if(name.equals(fridgeItems.get(k))){
                Toast.makeText(FridgeActivity.this, "Item is already in fridge", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;//doesn't already exist, add to list
    }

    private void addDBItem(){
        //Don't add anything if there is no user input, trim gets rid of empty spaces
        if(autocomplete.getText().toString().trim().length() == 0){
            return;
        }
        fridgeItems = dbHelper.getAllFridgeItems();
        String name = autocomplete.getText().toString();
        //prevents duplicate ingredients from being added to the fridge
        if(alreadyExistsTemp(name) == false){
            ContentValues cv = new ContentValues();
            cv.put(FridgeContract.FridgeEntry.COLUMN_NAME, name);
            //ID and timestamp occur automatically
            myDatabase.insert(FridgeContract.FridgeEntry.TABLE_NAME,null, cv);
            myAdapter.swapCursor(getAllItems());
        }

        autocomplete.getText().clear();
    }

    private Cursor getAllItems(){
        return myDatabase.query(
                FridgeContract.FridgeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FridgeContract.FridgeEntry._ID + " DESC"
        );
    }

    public void removeItem(long id){
        myDatabase.delete(FridgeContract.FridgeEntry.TABLE_NAME, FridgeContract.FridgeEntry._ID + "=" + id, null);
        myAdapter.swapCursor(getAllItems());
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
