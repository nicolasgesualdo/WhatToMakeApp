package com.example.whattomake3;

import java.util.*;

public class Recipe 
{
	private String name;
	private int difficulty;
	private int cookTime;
	private int numIngredients;
	private ArrayList<Ingredient> ingredients;
	private String instructions;
	private int image;
	private String category;
	
	public Recipe() 
	{
		name = "";
		difficulty = 0;
		cookTime = 0;
		numIngredients = 0;
		ingredients = new ArrayList<Ingredient>();
		instructions = "";
		image = R.drawable.ic_launcher_background;//default pic
		category = "";
	}
	
	public Recipe(String name, int difficulty, int cookTime, int numIngredients, ArrayList<Ingredient> theIngredients, String theInstructions, int theImage,String theCategory)
	{
		this.name = name;
		this.difficulty = difficulty;
		this.cookTime = cookTime;
		this.numIngredients = numIngredients;
		ingredients = new ArrayList<Ingredient>(theIngredients);
		instructions = theInstructions;
		image = theImage;
		category = theCategory;
	}
	
	public Recipe(Recipe aRecipe) {
		name = aRecipe.name;
		difficulty = aRecipe.difficulty;
		cookTime = aRecipe.cookTime;
		numIngredients = aRecipe.numIngredients;
		ingredients = new ArrayList<Ingredient>(aRecipe.ingredients);
		instructions = aRecipe.instructions;
		image = aRecipe.image;
		category = aRecipe.category;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public int getDifficulty() 
	{
		return difficulty;
	}

	public void setDifficulty(int difficulty) 
	{
		this.difficulty = difficulty;
	}

	public int getCookTime() 
	{
		return cookTime;
	}

	public void setCookTime(int cookTime) 
	{
		this.cookTime = cookTime;
	}
	
	public int getNumIngredients()
	{
		return numIngredients;
	}
	
	public void setNumIngredients(int numIngredients)
	{
		this.numIngredients = numIngredients;
	}

	public ArrayList<Ingredient> getIngredients()
	{
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> theIngredients)
	{
		ingredients = new ArrayList<Ingredient>(theIngredients);
	}

	public String getInstructions(){
		return instructions;
	}

	public void setInstructions(String theInstructions){
		instructions = theInstructions;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	//creates a string containing the entire arraylist
	public String printArrayList()
	{	
		String temp = "";
		for(Ingredient entry : ingredients)
		{
			temp += (entry.toString() + "\n");
		}
		return temp;
	}

	//creates an ingredient arraylist given a string of all ingredients for a recipe
	public void setIngArrayList(String allIngredients){
		String[] split = allIngredients.split("\n");
		ArrayList<Ingredient> temp = new ArrayList<>();

		for(int i = 0; i < split.length; i++){
			String[] split2 = split[i].split("\t");
			Ingredient ingredient = new Ingredient();
			if(split2.length==2 && !split2[1].equals(" ")){
				ingredient.setIngName(split2[0]);
				ingredient.setQuantity("-- " + split2[1]);
				temp.add(ingredient);
			}
			else{
				ingredient.setIngName(split2[0]);
				ingredient.setQuantity(" ");
				temp.add(ingredient);
			}
		}
        ingredients = new ArrayList<Ingredient>(temp);
	}

	//creates an ingredient arraylist given a string of all ingredients for a recipe
	public void setIngArrayListDashProblem(String allIngredients){
		String[] split = allIngredients.split("\n");
		ArrayList<Ingredient> temp = new ArrayList<>();

		for(int i = 0; i < split.length; i++){
			String[] split2 = split[i].split("\t");
			Ingredient ingredient = new Ingredient();
			if(split2.length==2 && !split2[1].equals(" ")){
				ingredient.setIngName(split2[0]);
				ingredient.setQuantity(split2[1]);
				temp.add(ingredient);
			}
			else{
				ingredient.setIngName(split2[0]);
				ingredient.setQuantity(" ");
				temp.add(ingredient);
			}
		}
		ingredients = new ArrayList<Ingredient>(temp);
	}
	
	public String toString()
	{
		return ("Name: " + name + "\n" + "Difficulty: " + difficulty + "\n" + "Cooking Time: " + cookTime + "\n" + "Ingredients: " + "\n" + printArrayList());
	}

}
