package uk.ac.tees.cupcake.healthnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.IntentUtils;

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
        holder.articleURL.setText(mNewsArticle.get(position).getArticleURL());
        holder.articleDate.setText(mNewsArticle.get(position).getArticleDate());

        holder.itemView.setOnClickListener(v -> IntentUtils.invokeToURL(mContext, mNewsArticle.get(position).getArticleURL()));
    }

    @Override
    public int getItemCount() {
        return mNewsArticle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView articleName;
        private TextView articleDesc;
        private TextView articleURL;
        private TextView articleDate;

        public ViewHolder(View itemView) {

            super(itemView);

            articleName = itemView.findViewById(R.id.article_title);
            articleDesc = itemView.findViewById(R.id.article_desc);
            articleURL = itemView.findViewById(R.id.article_url);
            articleDate = itemView.findViewById(R.id.article_date);
        }
    }
}
