package uk.ac.tees.cupcake.mealplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.food.SearchFoodActivity;

public class MealPlanner extends AppCompatActivity {

    private TextView addMealTextView;

    private String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private RecyclerView mRecycleView;

    private MealAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Meal> meals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);
        setTitle("Meal Tracker");
        totalCalories();

        addMealTextView = findViewById(R.id.mt_add_meal_text_view);

        addMealTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MealPlanner.this, SearchFoodActivity.class));
            }
        });

        mRecycleView = findViewById(R.id.mt_recycle_view);

        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new MealAdapter(meals);

        mRecycleView.setLayoutManager(mLayoutManager);

        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MealAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {

                FirebaseFirestore.getInstance().collection("Users")
                        .document(currentUserUid)
                        .collection("Meal Planner")
                        .document(meals.get(position).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MealPlanner.this, "Meal has been removed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    private void totalCalories(){

        double caloriesTotal = 0;
        for(int index = 0; index < meals.size(); index++){
            caloriesTotal += meals.get(index).getCalories();
        }

        TextView totalCalories = findViewById(R.id.mt_total_calories_text_view);
        totalCalories.setText("Total calories = " + caloriesTotal);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseFirestore.getInstance().collection("Users")
                .document(currentUserUid).collection("Meal Planner").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }

                for(DocumentChange dc : documentSnapshots.getDocumentChanges()){

                    Meal meal = dc.getDocument().toObject(Meal.class);

                    switch(dc.getType()){
                        case ADDED:
                            meals.add(meal);
                            mAdapter.notifyItemInserted(dc.getNewIndex());
                            mAdapter.notifyItemRangeInserted(dc.getNewIndex(), meals.size());
                            totalCalories();
                            break;

                        case REMOVED:
                            meals.remove(dc.getOldIndex());
                            mAdapter.notifyItemRemoved(dc.getOldIndex());
                            mAdapter.notifyItemRangeRemoved(dc.getOldIndex(), meals.size());
                            totalCalories();
                            break;

                    }
                }
            }
        });

    }
}
