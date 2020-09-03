package com.example.whattomake3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private SQLiteDatabase myDatabase;
    private SQLiteDatabase myFridgeDatabase;
    private SQLiteDatabase myRecipeDB;
    private ArrayList<Recipe> temp;
    private ListAdapter myAdapter;
    private ArrayList<String> allIngredients = new ArrayList<>();

    private Button listEnter;
    private EditText autocomplete;
    private Button addShoppingListToFridge;

    private ArrayList<String> listItems;
    private ListDatabaseHelper dbHelper;
    private ArrayList<String> fridgeItems;
    private FridgeDatabaseHelper dbFridgeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        dbHelper = new ListDatabaseHelper(this);//ListDatabaseHelper
        myDatabase = dbHelper.getWritableDatabase();
        listItems = dbHelper.getAllListItems();

        dbFridgeHelper = new FridgeDatabaseHelper(this);//FridgeDatabaseHelper
        myFridgeDatabase = dbFridgeHelper.getWritableDatabase();
        fridgeItems = dbFridgeHelper.getAllFridgeItems();

        RecipeDatabaseHelper dbRecipeHelper = new RecipeDatabaseHelper(this);
        myRecipeDB = dbRecipeHelper.getReadableDatabase();
        temp = new ArrayList<>();
        temp = dbRecipeHelper.getAllRecipes();

        //build RecyclerView
        RecyclerView theRecyclerView = findViewById(R.id.recyclerViewList);
        theRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ListAdapter(this, getAllItems());
        theRecyclerView.setAdapter(myAdapter);

        listEnter = findViewById(R.id.enterList);
        autocomplete = findViewById(R.id.autoCompleteTextViewList);
        addShoppingListToFridge = findViewById(R.id.addListToFridge);

        addAutocompleteList();

        AutoCompleteTextView editText = findViewById(R.id.autoCompleteTextViewList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,allIngredients);
        editText.setAdapter(adapter);

        //initialize variable
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNav.setSelectedItemId(R.id.nav_shoppingList);

        //select item listener for bottom bar
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        listEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDBItem();
            }
        });

        myAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(long id) {
                removeItem(id);
            }
        });

        addShoppingListToFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addListToFridge();
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
        return false;
    }

    private boolean alreadyExistsTemp(String name){
        for(int k = 0; k < listItems.size(); k++){
            if(name.equals(listItems.get(k))){
                Toast.makeText(ListActivity.this, "Item is already on shopping list", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;//doesn't already exist, add to list
    }

    private boolean alreadyExistsAddToFridge(String name){
        for(int k = 0; k < fridgeItems.size(); k++){
            if(name.equals(fridgeItems.get(k))){
                return true;
            }
        }
        return false;//doesn't already exist, add to list
    }

    private void addListToFridge(){
        Cursor cursor = myDatabase.rawQuery("select * from shoppinglist", null);

        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){//while true
                String temp = cursor.getString(1);
                long id = cursor.getLong(0);

                fridgeItems = dbFridgeHelper.getAllFridgeItems();
                //prevents duplicate ingredients from being added to the shopping list
                if(alreadyExistsAddToFridge(temp) == false){
                    ContentValues cv = new ContentValues();
                    cv.put(FridgeContract.FridgeEntry.COLUMN_NAME, temp);
                    //ID and timestamp occur automatically
                    myFridgeDatabase.insert(FridgeContract.FridgeEntry.TABLE_NAME,null, cv);
                }

                removeItem(id);
            }
        }
    }

    private void addDBItem(){
        //Don't add anything if there is no user input, trim gets rid of empty spaces
        if(autocomplete.getText().toString().trim().length() == 0){
            return;
        }

        listItems = dbHelper.getAllListItems();
        String name = autocomplete.getText().toString();
        //prevents duplicate ingredients from being added to the shopping list
        if(alreadyExistsTemp(name) == false){
            ContentValues cv = new ContentValues();
            cv.put(ListContract.ListEntry.COLUMN_NAME, name);
            //ID and timestamp occur automatically
            myDatabase.insert(ListContract.ListEntry.TABLE_NAME,null, cv);
            myAdapter.swapCursor(getAllItems());
        }

        autocomplete.getText().clear();
    }

    private Cursor getAllItems(){
        return myDatabase.query(
                ListContract.ListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ListContract.ListEntry._ID + " DESC"
        );
    }

    public void removeItem(long id){
        myDatabase.delete(ListContract.ListEntry.TABLE_NAME, ListContract.ListEntry._ID + "=" + id, null);
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
                    startActivity(new Intent(getApplicationContext(), FridgeActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.nav_shoppingList:
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
