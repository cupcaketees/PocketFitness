package uk.ac.tees.cupcake.feed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import uk.ac.tees.cupcake.R;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mFeedAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFeedAdapter = new FeedAdapter(Post.createPostsList(50));
        mRecyclerView.setAdapter(mFeedAdapter);

    }
}