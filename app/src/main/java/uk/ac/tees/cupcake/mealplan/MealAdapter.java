package uk.ac.tees.cupcake.mealplan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder>{

    private ArrayList<Meal> mMeals;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mKcal;
        private ImageButton deleteImageButton;

        public MealViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            mName = itemView.findViewById(R.id.meal_name);
            mKcal = itemView.findViewById(R.id.meal_kcal);
            deleteImageButton = itemView.findViewById(R.id.meal_remove_button);

            deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION);
                        listener.onDeleteClick(position);
                    }
                }
            });

        }
    }

    public MealAdapter(ArrayList<Meal> meals){
        this.mMeals = meals;
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_tracker_meal, parent, false);
        MealViewHolder mvh = new MealViewHolder(v, mListener);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MealViewHolder holder, int position) {
        Meal meal = mMeals.get(position);

        holder.mName.setText(meal.getName());

        String kcal = meal.getCalories() + " kcal";
        holder.mKcal.setText(kcal);
    }

    @Override
    public int getItemCount() {
       return mMeals.size();
    }

}
