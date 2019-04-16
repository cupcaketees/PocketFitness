package uk.ac.tees.cupcake.recipes;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.cupcake.R;

public class SearchRecipesActivity extends AppCompatActivity {

    private Map<String, Boolean> mFilterOptions = new HashMap<>();
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
     * OnClick that will search for recipes
     * @param view
     */
    public void searchRecipes(View view){

    }
}
