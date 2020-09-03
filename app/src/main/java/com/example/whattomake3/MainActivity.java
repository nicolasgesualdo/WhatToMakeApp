package com.example.whattomake3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button findRecipeBTN;
    private Button addRecipeBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findRecipeBTN = (Button)findViewById(R.id.findRecipeButton);
        addRecipeBTN = (Button)findViewById(R.id.addARecipe);

        //initialize variable
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNav.setSelectedItemId(R.id.nav_home);

        //select item listener for bottom bar
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        findRecipeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecipeDisplayActivity.class));
            }
        });

        addRecipeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomRecipeActivity.class));
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.nav_home:
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
