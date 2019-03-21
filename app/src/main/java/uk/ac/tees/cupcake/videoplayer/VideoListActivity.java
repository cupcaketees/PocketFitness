package uk.ac.tees.cupcake.videoplayer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;

/**
 * VideoList Activity
 *
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class VideoListActivity extends NavigationBarActivity {
    
    @Override
    public void setup() {
        stub.setLayoutResource(R.layout.activity_video_list);
        stub.inflate();

        RecyclerView mRecyclerView = findViewById(R.id.myRecycleView);
        ArrayList<Video> mVideos = new ArrayList<>();
        mVideos.add(new Video("Warm up", "The warming up is a preparation for physical exertion or a performance by exercising or practising gently beforehand.", R.drawable.temp_stretch, "link", "https://httpsak-a.akamaihd.net/4005328972001/4005328972001_5675453105001_5675442412001.mp4", "7.26"));
    
        mVideos.add(new Video("Aerobic Cardio", "Aerobic exercise is physical exercise of low to high intensity that depends primarily on the aerobic energy-generating process.",
                R.drawable.temp_man_running, "link", "https://httpsak-a.akamaihd.net/4005328972001/4005328972001_5675479616001_5675461827001.mp4", "12.11"));
    
        mVideos.add(new Video("Body Conditioning", "exercises that increase your strength, speed, endurance or any other physical attribute",
                R.drawable.temp_weight, "link", "https://httpsak-a.akamaihd.net/4005328972001/4005328972001_5675476796001_5675439266001.mp4", "14.50"));
    
        mVideos.add(new Video("Core Focus", "This beginner abs workout is fast and effective." +
                "Following a balanced core routine and starting with modified version of complex exercises will help you to build up your exercise confidence as you build up your abs.",
                R.drawable.temp_situp, "link", "https://httpsak-a.akamaihd.net/4005328972001/4005328972001_5468369588001_5401238075001.mp4", "7.15"));
    
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new VideoListAdapter(mVideos, getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}
