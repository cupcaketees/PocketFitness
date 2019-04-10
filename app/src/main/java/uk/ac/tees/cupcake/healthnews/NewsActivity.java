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
    protected int layoutResource() {
        return R.layout.news_list_view;
    }

    @Override
    public void setup() {

        RecyclerView newsRecycler = findViewById(R.id.news_articles_list);

        newsArticles.add(new NewsArticle("World's Strongest Man 2019 Winner",
                "Thor Bjornson declared world's strongest man 2019 after a close competition.","12/03/2019", "'https://www.menshealth.com/uk/building-muscle/a759056/5-finalists-to-watch-at-the-2018-worlds-strongest-man/'"));

        newsArticles.add(new NewsArticle("Larry Wheels talks steroids",
                "Larry Wheels describes his first experience with a steroid cycle as rats eating away at his intestines.", "11/02/2019", "'https://barbend.com/larry-wheels-steroids/'"));

        newsArticles.add(new NewsArticle("New superfood",
                "Golden berries, these are deliciously sweet options are solid sources of fiber," +
                        " plus they’re rich in antioxidants and heart-healthy fatty acids. ", "24/01/2019", "'https://www.cookinglight.com/news/best-superfoods-for-health'"));

        newsRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new NewsAdapter(newsArticles, this);
        newsRecycler.setLayoutManager(layoutManager);
        newsRecycler.setAdapter(adapter);

    }
}
