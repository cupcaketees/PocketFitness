package uk.ac.tees.cupcake.recipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;

/**
 * Recipe search results activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */

public class RecipeSearchResultsActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipes = new ArrayList<>();
    private RecyclerView rvRecipes;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_result);

        rvRecipes = findViewById(R.id.rvRecipes);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            String url = extras.getString("url");
            setTitle("Search results for " + extras.get("title"));
            sendRequest(url);

            System.out.println("URL = " + url);
        }
    }
    /**
     * @param url
     */
    private void sendRequest(String url){

        RequestQueue queue = Volley.newRequestQueue(RecipeSearchResultsActivity.this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                recipes = Recipe.fromJSONArray(response);

                mRecipeAdapter = new RecipeAdapter(recipes);
                rvRecipes.setAdapter(mRecipeAdapter);
                rvRecipes.setLayoutManager(new GridLayoutManager(RecipeSearchResultsActivity.this,2));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}
