package uk.ac.tees.cupcake.food;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
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
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mFoodLabel;
        public TextView mFoodCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            mFoodLabel = itemView.findViewById(R.id.search_food_row_label_value_text_view);
            mFoodCategory = itemView.findViewById(R.id.search_food_row_category_value_text_view);
        }
    }
}
