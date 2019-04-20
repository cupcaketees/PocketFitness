package uk.ac.tees.cupcake.recipes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.Map;

import uk.ac.tees.cupcake.R;

/**
 * Recipe item activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */

public class RecipeItemActivity extends AppCompatActivity {

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_item);

        // Initialise

        TextView recipeNameTextView = findViewById(R.id.recipe_item_name_text_view);
        ImageView recipeImageImageView = findViewById(R.id.recipe_item_image_image_view);
        TextView recipeHealthLabelsTextView = findViewById(R.id.recipe_item_health_label_results_text_view);
        TextView recipeDietLabelsTextView = findViewById(R.id.recipe_item_diet_label_results_text_view);
        TextView recipeSourceTextView = findViewById(R.id.recipe_item_source_text_view_link);
        TextView recipeIngredientsTextView = findViewById(R.id.recipe_item_ingredients_results_text_view);
        TextView recipeCaloriesTextView = findViewById(R.id.recipe_item_calories_result_text_view);
        TextView recipeWeightTextView = findViewById(R.id.recipe_item_weight_result_text_view);
        TextView recipeServingsTextView = findViewById(R.id.recipe_item_servings_result_text_view);
        TextView recipeTimeTextView = findViewById(R.id.recipe_item_time_result_text_view);

        // Get
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        // Set

        // Title
        setTitle(recipe.getLabel());
        recipeNameTextView.setText(recipe.getLabel());

        // Health labels
        String[] healthLabels = recipe.getHealthLabels();

        if(healthLabels.length == 0){
            recipeHealthLabelsTextView.setText("No Health labels");
        }

        for(int i = 0 ; i < healthLabels.length ; i++){
            recipeHealthLabelsTextView.append(healthLabels[i].toString() + "\n");
        }

        // Diet labels

        String[] dietLabels = recipe.getDietLabels();

        for(int i = 0 ; i < dietLabels.length ; i++){
            recipeDietLabelsTextView.append(healthLabels[i].toString() + "\n");
        }

        if(dietLabels.length == 0){
            recipeDietLabelsTextView.setText("No Diet labels");
        }

        // Ingredients
        Map<String,Double> ingredients = recipe.getIngredients();

        if(ingredients.size() == 0){
            recipeIngredientsTextView.setText("No ingredients available");
        }

        Iterator ingredientIt = ingredients.keySet().iterator();

        while(ingredientIt.hasNext()){
            String key = (String) ingredientIt.next();
            recipeIngredientsTextView.append(key + " - " + ingredients.get(key) + " g\n\n" );
        }

        // Url and Source

        mUrl = recipe.getUrl();
        recipeSourceTextView.setText(recipe.getSource());

        // Image
        Picasso.with(RecipeItemActivity.this).load(recipe.getImageUrl()).into(recipeImageImageView);

        // Statistics
        recipeCaloriesTextView.setText("Calories " + recipe.getTotalCalories().intValue() + " kcal");

        recipeWeightTextView.setText("Weight " + recipe.getTotalWeight() + " g");

        recipeServingsTextView.setText("Servings " + recipe.getYield());

        recipeTimeTextView.setText("Time " + recipe.getTotalTime() + " minutes");

        if(recipe.getTotalTime() == 0){
            recipeTimeTextView.setText("Time N/A");
        }


    }

    public void browseSourceOnClick(View view){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
            startActivity(intent);
        }catch(ActivityNotFoundException e){
            e.printStackTrace();
        }
    }
}
