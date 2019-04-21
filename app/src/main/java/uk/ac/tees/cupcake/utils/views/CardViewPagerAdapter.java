package uk.ac.tees.cupcake.utils.views;

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

import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.health.Exercise;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class CardViewPagerAdapter extends PagerAdapter {

    private final List<Exercise> exercises;

    public CardViewPagerAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public int getCount() {
        return exercises.size();
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
        TextView titleTextView = cardView.findViewById(R.id.exercise_selection_item_title);
        ImageView imageView = cardView.findViewById(R.id.exercise_selection_item_image_view);
        
        Exercise exercise = exercises.get(position);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), exercise.getDrawableId());
        
        titleTextView.setText(exercise.getTitle());
        imageView.setImageDrawable(drawable);
        imageView.setColorFilter(0xFF757575, PorterDuff.Mode.SRC_ATOP);
    }

}