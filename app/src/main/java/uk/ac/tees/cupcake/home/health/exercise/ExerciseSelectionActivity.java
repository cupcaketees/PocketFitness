package uk.ac.tees.cupcake.home.health.exercise;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.views.CardViewPagerAdapter;

public class ExerciseSelectionActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection);
    
        final int margin = (int) (16 * getResources().getDisplayMetrics().density);
    
        HorizontalScrollView scrollView = findViewById(R.id.exercise_selection_background_scroll_view);
        scrollView.setOnTouchListener((v, event) -> true);
        
        ImageView backgroundImage = findViewById(R.id.exercise_selection_background);
        backgroundImage.invalidate();
        
        ViewPager viewPager = findViewById(R.id.exercise_selection_view_pager);
        viewPager.setAdapter(new CardViewPagerAdapter());
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