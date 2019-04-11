package uk.ac.tees.cupcake.healthnews;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;

/**
 * News Activity
 * Show user relevant fitness/health news
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 * @author Hugo Tomas <s6006225@tees.ac.uk> - News API
 */
public class NewsActivity extends NavigationBarActivity {
    private static final String TAG = "NewsActivity";
    private ArrayList<NewsArticle> newsArticles = new ArrayList<>();
    private ProgressBar mProgressBar;
    private TextView mCheckNewsRecieved;
    @Override
    protected int layoutResource() {
        return R.layout.news_list_view;
    }

    /**
     * Retrieve API Key from String Resources
     * Gets all news related to fitness but caps at 50.
     * Adds articles to ArrayList then sends to {@link NewsAdapter}.
     */
    @Override
    public void setup() {

        mProgressBar = findViewById(R.id.news_progressbar);
        mCheckNewsRecieved = findViewById(R.id.news_availability);

        mProgressBar.setVisibility(View.VISIBLE);
        mCheckNewsRecieved.setVisibility(View.GONE);

        NewsApiClient newsApiClient = new NewsApiClient(getString(R.string.news_api_key));

        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("Fitness")
                        .pageSize(50)
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {

                    @Override
                    public void onSuccess(ArticleResponse articleResponse) {

                        for (int i = 0; i < 50; i++) {
                            newsArticles.add(new NewsArticle(articleResponse.getArticles().get(i).getTitle(),
                                    articleResponse.getArticles().get(i).getDescription(),
                                    articleResponse.getArticles().get(i).getPublishedAt(),
                                    articleResponse.getArticles().get(i).getSource().getName(),
                                    articleResponse.getArticles().get(i).getUrlToImage(),
                                    articleResponse.getArticles().get(i).getUrl()));
                        }

                        RecyclerView newsRecycler = findViewById(R.id.news_articles_list);
                        Log.d(TAG, "setup: " + newsArticles.size());
                        newsRecycler.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        RecyclerView.Adapter adapter = new NewsAdapter(newsArticles, getApplicationContext());
                        newsRecycler.setLayoutManager(layoutManager);
                        newsRecycler.setAdapter(adapter);
                        mProgressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d(TAG, "onFailure: " + throwable);
                        System.out.println(throwable.getMessage());
                        mProgressBar.setVisibility(View.GONE);
                        mCheckNewsRecieved.setVisibility(View.VISIBLE);

                    }
                }
        );

    }
}
