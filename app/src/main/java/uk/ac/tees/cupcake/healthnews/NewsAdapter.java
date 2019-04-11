package uk.ac.tees.cupcake.healthnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.IntentUtils;

import static com.android.volley.VolleyLog.TAG;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final ArrayList<NewsArticle> mNewsArticle;
    private final Context mContext;

    public NewsAdapter(ArrayList<NewsArticle> mNewsArticle, Context mContext) {
        this.mNewsArticle = mNewsArticle;
        this.mContext = mContext;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_articles, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        holder.articleDesc.setText(mNewsArticle.get(position).getArticleDesc());
        holder.articleName.setText(mNewsArticle.get(position).getArticleName());
        holder.articleURL.setText(mNewsArticle.get(position).getArticleURLName());
        holder.articleDate.setText(dateConfiguration(mNewsArticle.get(position).getArticleDate()));

        Log.d(TAG, "onBindViewHolder: " + mNewsArticle.get(position).getArticleURLName());

        Picasso
                .with(mContext)
                .load(mNewsArticle.get(position).getArticleImageURL())
                .resize(380, 200)
                .into(holder.articleImage);

        holder.itemView.setOnClickListener(v -> IntentUtils.invokeToURL(mContext, mNewsArticle.get(position).getArticleURLLink()));
    }

    @Override
    public int getItemCount() {
        return mNewsArticle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView articleName;
        private TextView articleDesc;
        private TextView articleURL;
        private ImageView articleImage;
        private TextView articleDate;

        public ViewHolder(View itemView) {

            super(itemView);

            articleName = itemView.findViewById(R.id.article_title);
            articleDesc = itemView.findViewById(R.id.article_desc);
            articleImage = itemView.findViewById(R.id.article_image);
            articleURL = itemView.findViewById(R.id.article_url);
            articleDate = itemView.findViewById(R.id.article_date);
        }
    }

    public String dateConfiguration(String Date) {

        return Date.replace("T", " ").replace("Z", "");
    }
}
