package uk.ac.tees.cupcake.recipes;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import uk.ac.tees.cupcake.R;

/**
 * Search Recipes Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class SearchRecipesActivity extends AppCompatActivity {

    private Map<String, Boolean> mFilterOptions = new HashMap<>();
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ConstraintLayout mRefineSearchConstraintLayout;
    private EditText mSearchBarEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);

        // Initialise
        mRefineSearchConstraintLayout = findViewById(R.id.refine_search_constraint_layout);
        mSearchBarEditText = findViewById(R.id.search_recipe_edit_text);

        // Set value
        mRefineSearchConstraintLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * On click for when a check box is clicked.
     * Removes unnecessary part of resource id, adds to mFilterOptions map as key, isChecked to value/
     * @param view
     */
    public void onCheckBoxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        String key = getResources().getResourceEntryName(view.getId());
        // Removes unnecessary part of string
        key = key.replaceAll("refine_search_", "");

        mFilterOptions.put(key,checked);
    }

    /**
     * OnClick that sets visibility of constraint layout accordingly
     * @param view
     */
    public void filterSearchOnClick(View view){
         int value = mRefineSearchConstraintLayout.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE;
         mRefineSearchConstraintLayout.setVisibility(value);
    }

    /**
     * OnClick that will build string for edamam get request.
     * @param view
     */
    public void buildString(View view){
        String searchInput = mSearchBarEditText.getText().toString().trim();
        StringBuilder bldr = new StringBuilder("https://api.edamam.com/search?q=");

        if(TextUtils.isEmpty(searchInput)){
            Toast.makeText(SearchRecipesActivity.this, "You must enter a search value", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            // Encodes search input
            URLEncoder.encode(searchInput, "utf-8");

            bldr.append(searchInput)
                .append("&app_id=09d06045&app_key=dd8b097670ff8e9fd118eb3dd8e2d0cb&from=0&to=50");

            //Iterates map to get added filter types and api parameters
            for (Map.Entry<String, Boolean> e : mFilterOptions.entrySet()) {
                if (e.getValue()) {
                    String key = e.getKey();

                    // separates string to type and api param.
                    StringTokenizer token = new StringTokenizer(key, "_");
                    String type = token.nextToken();
                    String apiParam = token.nextToken();

                    // appends type and corresponding api parameter to string
                    bldr.append("&")
                        .append(type)
                        .append("=")
                        .append(apiParam);
                }
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        // Sends user to recipe result extra
        Intent intent = new Intent(SearchRecipesActivity.this, RecipeSearchResultsActivity.class);
        intent.putExtra("url" , bldr.toString());
        intent.putExtra("title", searchInput);
        startActivity(intent);
    }

}
