package uk.ac.tees.cupcake.videoplayer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;

/**
 * VideoList Activity
 *
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class VideoListActivity extends NavigationBarActivity {

    private RecyclerView.Adapter mAdapter;

    @Override
    protected int layoutResource() {
        return R.layout.activity_recycler_view;
    }
    
    @Override
    public void setup() {
        ArrayList<Video> mVideos = new ArrayList<>();

        RecyclerView mRecyclerView = findViewById(R.id.myRecycleView);
        mVideos.add(new Video("Warm up", "The warming up is a preparation for physical exertion or a performance by exercising or practising gently beforehand.",
                R.drawable.temp_stretch, "link",
                "https://firebasestorage.googleapis.com/v0/b/cupcaketees-26df6.appspot.com/o/workout%20videos%2FWarm%20up.mp4?alt=media&token=ec60d9e3-1a5a-4b1c-ae9c-3395e3577a0f",
                "7.26"));
    
        mVideos.add(new Video("Aerobic Cardio", "Aerobic exercise is physical exercise of low to high intensity that depends primarily on the aerobic energy-generating process.",
                R.drawable.temp_man_running, "link",
                "https://firebasestorage.googleapis.com/v0/b/cupcaketees-26df6.appspot.com/o/workout%20videos%2FAerobic%20%20cardio.mp4?alt=media&token=d331afa3-a729-4b9e-9b18-d027bfa73cc5",
                "12.11"));
    
        mVideos.add(new Video("Body Conditioning", "exercises that increase your strength, speed, endurance or any other physical attribute",
                R.drawable.temp_weight, "link", "https://firebasestorage.googleapis.com/v0/b/cupcaketees-26df6.appspot.com/o/workout%20videos%2FBody%20conditioning.mp4?alt=media&token=56298f9e-2415-4866-b59f-b3292474f2cb",
                "14.50"));
    
        mVideos.add(new Video("Core Focus", "This beginner abs workout is fast and effective." +
                "Following a balanced core routine and starting with modified version of complex exercises will help you to build up your exercise confidence as you build up your abs.",
                R.drawable.temp_situp, "link", "https://firebasestorage.googleapis.com/v0/b/cupcaketees-26df6.appspot.com/o/workout%20videos%2FCore%20focus.mp4?alt=media&token=1240ede9-6115-458a-8ce0-a419ea9f5144",
                "7.15"));

        mAdapter = new VideoListAdapter(mVideos, getApplicationContext());
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Enter Workout");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((VideoListAdapter) mAdapter).getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}
