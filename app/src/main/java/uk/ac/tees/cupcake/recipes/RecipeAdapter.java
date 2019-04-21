package uk.ac.tees.cupcake.recipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.ac.tees.cupcake.R;

/**
 * Recipe Adapter
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener newListener){
        mListener = newListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView recipeNameTextView;
        private TextView recipeSourceTextView;
        private TextView recipeCaloriesTextView;
        private ImageView recipeImageView;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            recipeNameTextView = itemView.findViewById(R.id.recipe_name_text_view);
            recipeSourceTextView = itemView.findViewById(R.id.recipe_source_text_view);
            recipeCaloriesTextView = itemView.findViewById(R.id.recipe_calories_text_view);
            recipeImageView = itemView.findViewById(R.id.recipe_image_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    private List<Recipe> mRecipes;

    public RecipeAdapter(List<Recipe> recipes){
        mRecipes = recipes;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeView = inflater.inflate(R.layout.recipe_row_rv, parent, false);

        return new ViewHolder(recipeView, mListener);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        // Get
        ImageView image = holder.recipeImageView;
        TextView source = holder.recipeSourceTextView;
        TextView calories = holder.recipeCaloriesTextView;
        TextView name = holder.recipeNameTextView;

        // Set
        name.setText(recipe.getLabel());
        source.setText(recipe.getSource());

        String caloriesText = recipe.getTotalCalories().intValue() / recipe.getYield() + " kcal / " + recipe.getYield() + " servings";
        calories.setText(caloriesText);

        Picasso.with(holder.itemView.getContext()).load(recipe.getImageUrl()).into(image);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }
}
