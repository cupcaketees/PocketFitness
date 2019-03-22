package uk.ac.tees.cupcake.food;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.util.ArrayList;
import uk.ac.tees.cupcake.R;
import java.util.List;

/**
 * Created by /**
 * {@author Bradley Hunter < s6263464@live.tees.ac.uk}
 * {@author Sam Hammersley < q5315908@live.tees.ac.uk}
 */

public class SearchFoodActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Food> foods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        setTitle("SearchFood");

        mRecyclerView = findViewById(R.id.search_food_recycler_view);

        //layout of items
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchFoodActivity.this);

        // setup recycler view
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new FoodAdapter(foods));
    }

    public void searchFood(View view){
        EditText searchEditText = findViewById(R.id.search_bar_edit_text);

        String userInput = searchEditText.getText().toString().trim();

        if(TextUtils.isEmpty(userInput)){
            Toast.makeText(SearchFoodActivity.this, "You must enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(SearchFoodActivity.this);
        try{
            String input = URLEncoder.encode(userInput, "utf-8");

            //todo app key should be hidden
            String url = "https://api.edamam.com/api/food-database/parser?ingr=" + input + "&app_id=824ccde8&app_key=df089d0c12a5a75b413a7a21a0a5a9e2";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                foods = Food.fromJSONObject(response);
                mRecyclerView.setAdapter(new FoodAdapter(foods));
            }, error -> Toast.makeText(SearchFoodActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show());

            queue.add(stringRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}