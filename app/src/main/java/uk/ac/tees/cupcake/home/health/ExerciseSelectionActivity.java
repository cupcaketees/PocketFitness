package uk.ac.tees.cupcake.home.health;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.views.CardViewPagerAdapter;

public class ExerciseSelectionActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection);
        
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise("Walking", "walk lol", R.drawable.exercise_selection_walking));
        exercises.add(new Exercise("Running", "run lol", R.drawable.exercise_selection_running));
        exercises.add(new Exercise("Cycling", "on ur bike pal", R.drawable.exercise_selection_cycling));
    
        final int margin = (int) (16 * getResources().getDisplayMetrics().density);
    
        HorizontalScrollView scrollView = findViewById(R.id.exercise_selection_background_scroll_view);
        scrollView.setOnTouchListener((v, event) -> true);
        
        ImageView backgroundImage = findViewById(R.id.exercise_selection_background);
        backgroundImage.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) backgroundImage.getDrawable();
        
        Blurry.with(this).radius(25).sampling(2).from(drawable.getBitmap()).into(backgroundImage);
        
        ViewPager viewPager = findViewById(R.id.exercise_selection_view_pager);
        viewPager.setAdapter(new CardViewPagerAdapter(exercises));
        viewPager.setPageMargin(margin);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int x = (int) ((viewPager.getWidth() * (position + positionOffset)) * computeFactor());

                scrollView.smoothScrollTo(x, 0);
            }
            
            @Override
            public void onPageSelected(int position) {

            }
        
            @Override
            public void onPageScrollStateChanged(int state) {}
        
            private float computeFactor() {
                return (float) ((backgroundImage.getWidth() - viewPager.getWidth()) / viewPager.getWidth());
            }
        });
        
    }
}