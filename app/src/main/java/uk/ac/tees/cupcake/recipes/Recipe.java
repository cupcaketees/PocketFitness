package uk.ac.tees.cupcake.recipes;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Recipe Class
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class Recipe implements Serializable {

    private String label;
    private String imageUrl;
    private String source;
    private Double totalCalories;
    private Double totalWeight;
    private int yield;
    private String[] healthLabels;
    private String[] dietLabels;
    private String url;
    private Map<String,Double>  ingredients;
    private int totalTime;

    public Recipe(String label , String imageUrl, String source, Double calories, int yield, String[] healthLabels, String[] dietLabels, String url, Map<String,Double> ingredients, Double weight, int time){
        this.label = label;
        this.imageUrl = imageUrl;
        this.source = source;
        this.totalCalories = calories;
        this.yield = yield;
        this.healthLabels = healthLabels;
        this.dietLabels = dietLabels;
        this.url = url;
        this.ingredients = ingredients;
        this.totalWeight = weight;
        this.totalTime = time;
    }

    public String getLabel() {
        return label;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getSource() {
        return source;
    }
    public Double getTotalCalories() {
        return totalCalories;
    }
    public int getYield() {
        return yield;
    }
    public String[] getHealthLabels() { return healthLabels; }
    public String[] getDietLabels() { return dietLabels; }
    public String getUrl() { return url; }
    public Map<String,Double>  getIngredients() { return ingredients; }
    public Double getTotalWeight() { return totalWeight; }
    public int getTotalTime() { return totalTime; }

    public static ArrayList<Recipe> fromJSONArray(JSONObject obj){
        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray result = obj.getJSONArray("hits");

            for(int i = 0; i < result.length(); i++){

                JSONObject recipeObject = result.getJSONObject(i).getJSONObject("recipe");

                String label = recipeObject.getString("label");
                String imageUrl = recipeObject.getString("image");
                String source = recipeObject.getString("source");
                Double calories = recipeObject.getDouble("calories");
                int yield = recipeObject.getInt("yield");
                String url = recipeObject.getString("url");
                Double totalWeight = recipeObject.getDouble("totalWeight");
                int totalTime = recipeObject.getInt("totalTime");


                // Health labels
                JSONArray arrHealthLabels  = recipeObject.getJSONArray("healthLabels");

                String[] healthLabelsValues = new String[arrHealthLabels.length()];

                for (int z = 0; z < arrHealthLabels.length(); z++){
                    healthLabelsValues[z] = arrHealthLabels.getString(z);
                }

                // Diet labels
                JSONArray arrDietLabels = recipeObject.getJSONArray("dietLabels");

                String[] dietLabelsValues = new String[arrDietLabels.length()];

                for(int z = 0; z < arrDietLabels.length(); z++){
                    dietLabelsValues[z] = arrDietLabels.getString(z);
                }

                // Ingredients

                JSONArray arrIngredients = recipeObject.getJSONArray("ingredients");

                Map<String, Double> ingredientValues = new HashMap<>();

                for(int z = 0; z < arrIngredients.length(); z++){

                    String objIngredientsTextValue = arrIngredients.getJSONObject(z).getString("text");
                    Double objIngredientsWeightValue = arrIngredients.getJSONObject(z).getDouble("weight");

                    ingredientValues.put(objIngredientsTextValue,objIngredientsWeightValue);
                }

                Recipe newRecipe = new Recipe(label, imageUrl, source, calories, yield, healthLabelsValues, dietLabelsValues, url, ingredientValues, totalWeight, totalTime);
                recipes.add(newRecipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
