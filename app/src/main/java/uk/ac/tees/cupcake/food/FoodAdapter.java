package uk.ac.tees.cupcake.food;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import uk.ac.tees.cupcake.R;

/**
 * Created by /**
 * {@author Bradley Hunter < s6263464@live.tees.ac.uk}
 * {@author Sam Hammersley < q5315908@live.tees.ac.uk}
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private final List<Food> foods;

    public FoodAdapter(List<Food> foods){
        this.foods = foods;
    }

    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_food_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FoodAdapter.ViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.mFoodLabel.setText(food.getLabel());
        holder.mFoodCategory.setText(food.getCategory());
        setNutritionalValuesText(food.getNutritionalValues(), holder.mFoodNutritionLeft, holder.mFoodNutritionRight);
    }

    private void setNutritionalValuesText(Map<String, Double> nutritionalValues, TextView column1, TextView column2) {
        StringBuilder columnText1 = new StringBuilder();
        StringBuilder columnText2 = new StringBuilder();

        int index = 0;
        for (Map.Entry<String, Double> entry : nutritionalValues.entrySet()) {
            String readableLabel = Food.getReadableLabel(entry.getKey());
            (index++ % 2 == 0 ? columnText1 : columnText2)
                    .append("\n")
                    .append(readableLabel)
                    .append(":  ")
                    .append(String.format("%.2f", entry.getValue()));
        }
        column1.setText(columnText1);
        column2.setText(columnText2);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mFoodLabel;
        private TextView mFoodCategory;
        private TextView mFoodNutritionLeft;
        private TextView mFoodNutritionRight;

        public ViewHolder(View itemView) {
            super(itemView);
            mFoodLabel = itemView.findViewById(R.id.search_food_row_label_value_text_view);
            mFoodCategory = itemView.findViewById(R.id.search_food_row_category_value_text_view);
            mFoodNutritionLeft = itemView.findViewById(R.id.search_food_row_nutrition_label_left_text_view);
            mFoodNutritionRight = itemView.findViewById(R.id.search_food_row_nutrition_label_right_text_view);
        }
    }
}
