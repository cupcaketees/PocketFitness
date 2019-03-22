package uk.ac.tees.cupcake.healthnews;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;
/**
 * Bodybuilding Workout Activity Class
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class NewsActivity extends NavigationBarActivity {

    private ArrayList<NewsArticle> newsArticles = new ArrayList<>();

    @Override
    public void setup() {
        stub.setLayoutResource(R.layout.news_articles);
        stub.inflate();

        RecyclerView newsRecycler = findViewById(R.id.news_list_view);

        newsArticles.add(new NewsArticle("World's Strongest Man 2019 Winner", "Thor Bjornson declared world's strongest man 2019 after a close competition", R.drawable.temp_man_running, "12/03/2019"));

        newsArticles.add(new NewsArticle("Larry Wheels talks steroids", "Larry Wheels describes his first experience with a steroid cycle as rats eating away at his intestines.", R.drawable.temp_man_running, "11/02/2019"));

        newsRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new NewsAdapter(newsArticles, this);
        newsRecycler.setLayoutManager(layoutManager);
        newsRecycler.setAdapter(adapter);
    }
}
