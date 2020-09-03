package com.example.whattomake3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {

    private ArrayList<Recipe> allRecipes;
    private RecipeAdapter myAdapter;
    private SQLiteDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        RecipeDatabaseHelper dbHelperRecipe = new RecipeDatabaseHelper(this);
        myDatabase = dbHelperRecipe.getWritableDatabase();
        allRecipes = new ArrayList<>();
        allRecipes = dbHelperRecipe.getAllRecipes();//populates arraylist with recipe objects from database

        buildRecyclerView();

        myAdapter.setOnItemLongClickListener(new RecipeAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(final long id) {
                Toast.makeText(RecipesActivity.this,"Long Click", Toast.LENGTH_SHORT).show();
                final long id2 = id;
                //Delete Alert
                AlertDialog.Builder deletealert = new AlertDialog.Builder(RecipesActivity.this);

                deletealert.setTitle("Delete Confirmation");
                deletealert.setMessage("Are you sure you want to delete recipe");
                deletealert.setCancelable(true);

                deletealert.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(RecipesActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        removeItem(id2);
                    }
                });

                deletealert.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(RecipesActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                //Show dialog
                deletealert.create().show();

            }
        });

        //initialize variable
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNav.setSelectedItemId(R.id.nav_recipes);

        //select item listener for bottom bar
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private void buildRecyclerView(){
        RecyclerView theRecyclerView = findViewById(R.id.recyclerViewRecipe);
        theRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        myAdapter = new RecipeAdapter(this, allRecipes);
        theRecyclerView.setAdapter(myAdapter);
    }

    public void removeItem(long id){
        String name = allRecipes.get((int)id).getName();
        myDatabase.delete(RecipeContract.RecipeEntry.TABLE_NAME, RecipeContract.RecipeEntry.COLUMN_NAME + "='" + name + "'", null);
        allRecipes.remove((int)id);
        myAdapter.notifyDataSetChanged();
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
                    return true;
            }
            return false;
        }
    };
}
