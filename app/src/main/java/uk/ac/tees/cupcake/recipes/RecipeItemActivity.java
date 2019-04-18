package uk.ac.tees.cupcake.recipes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import uk.ac.tees.cupcake.R;

/**
 * Recipe item activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */

public class RecipeItemActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_item);

        TextView recipeNameTextView = findViewById(R.id.recipe_item_name_text_view);
        ImageView recipeImageImageView = findViewById(R.id.recipe_item_image_image_view);

        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        setTitle(recipe.getLabel());

        recipeNameTextView.setText(recipe.getLabel());
        Picasso.with(RecipeItemActivity.this).load(recipe.getImageUrl()).into(recipeImageImageView);
    }
}
