package uk.ac.tees.cupcake.recipes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Recipe Class
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class Recipe implements Serializable {

    private String label;
    private String imageUrl;
    private String source;
    private Double calories;
    private int yield;

    public Recipe(String label , String imageUrl, String source, Double calories, int yield){
        this.label = label;
        this.imageUrl = imageUrl;
        this.source = source;
        this.calories = calories;
        this.yield = yield;
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
    public Double getCalories() {
        return calories;
    }
    public int getYield() {
        return yield;
    }

    public static ArrayList<Recipe> fromJSONArray(JSONObject obj){
        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray result = obj.getJSONArray("hits");

            for(int i = 0; i < result.length(); i++){

                JSONObject matches = result.getJSONObject(i).getJSONObject("recipe");

                String label = matches.getString("label");
                String imageUrl = matches.getString("image");
                String source = matches.getString("source");
                double calories = matches.getDouble("calories");
                int yield = matches.getInt("yield");

                Recipe recipe = new Recipe(label, imageUrl, source, calories, yield);
                recipes.add(recipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
