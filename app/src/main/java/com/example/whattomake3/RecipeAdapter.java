package com.example.whattomake3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context myContext;
    private ArrayList<Recipe> recipeArrayList;

    public RecipeAdapter(Context myContext, ArrayList<Recipe> recipeArrayList){
        this.myContext = myContext;
        this.recipeArrayList = recipeArrayList;
    }

    private OnItemLongClickListener myListener;

    public interface OnItemLongClickListener{
        void OnItemLongClick(long id);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        myListener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View v = inflater.inflate(R.layout.recipe_item, parent,false);
        RecipeAdapter.RecipeViewHolder fvh = new RecipeAdapter.RecipeViewHolder(v, myListener);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {
        holder.myRecipeTitle.setText(recipeArrayList.get(position).getName());
        holder.myRecipeImage.setImageResource(recipeArrayList.get(position).getImage());

        holder.recipeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext,DisplayAllRecipeActivity.class);

                intent.putExtra("Name", recipeArrayList.get(position).getName());
                intent.putExtra("Pic", recipeArrayList.get(position).getImage());
                intent.putExtra("Difficulty", recipeArrayList.get(position).getDifficulty());
                intent.putExtra("CookTime", recipeArrayList.get(position).getCookTime());
                intent.putExtra("NumIng", recipeArrayList.get(position).getNumIngredients());
                intent.putExtra("Category", recipeArrayList.get(position).getCategory());
                intent.putExtra("Ingredients", recipeArrayList.get(position).printArrayList());
                intent.putExtra("Instructions", recipeArrayList.get(position).getInstructions());

                myContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView myRecipeTitle;
        ImageView myRecipeImage;
        CardView recipeCard;

        //Constructor
        public RecipeViewHolder(@NonNull final View itemView, final OnItemLongClickListener listener) {
            super(itemView);
            myRecipeTitle = itemView.findViewById(R.id.recipeTitle);
            myRecipeImage = itemView.findViewById(R.id.recipeImage);
            recipeCard = itemView.findViewById(R.id.recipeCardView);

            recipeCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null) {
                        long position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.OnItemLongClick(position);
                        }
                    }
                    return false;
                }
            });
        }
    }
}
