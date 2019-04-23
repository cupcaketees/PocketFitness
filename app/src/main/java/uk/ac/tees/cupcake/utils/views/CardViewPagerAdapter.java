package uk.ac.tees.cupcake.utils.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.health.exercise.Exercise;
import uk.ac.tees.cupcake.home.health.exercise.ExerciseMapActivity;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class CardViewPagerAdapter extends PagerAdapter {

    private final Exercise[] exercises = Exercise.values();
    
    @Override
    public int getCount() {
        return exercises.length;
    }
    
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public CardView instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.exercise_selection_item_layout, container, false);
        
        container.addView(view);
        
        CardView cardView = view.findViewById(R.id.cardView);
        bind(cardView, position);

        return cardView;
    }
    
    private void bind(View cardView, int position) {
        cardView.setEnabled(true);
        TextView titleTextView = cardView.findViewById(R.id.exercise_selection_item_title);
        ImageView imageView = cardView.findViewById(R.id.exercise_selection_item_image_view);
        
        Exercise exercise = exercises[position];
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), exercise.getDrawableId());
        
        titleTextView.setText(exercise.getName());
        imageView.setImageDrawable(drawable);
        imageView.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_ATOP);
        
        cardView.setOnClickListener(v -> {
            Context context = v.getContext();
    
            context.startActivity(new Intent(context, ExerciseMapActivity.class).putExtra("exercise", exercise));
        });
    }

}