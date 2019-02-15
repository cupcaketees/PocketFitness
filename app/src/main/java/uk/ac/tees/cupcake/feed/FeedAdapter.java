package uk.ac.tees.cupcake.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 12/02/2019.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<Post> posts;

    public FeedAdapter(List<Post> posts){
        this.posts = posts;
        setHasStableIds(true);
    }

    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.post_layout, parent, false);

        return new FeedViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.descriptionTextView.setText(post.getDescription());
        holder.usernameTextView.setText(post.getUsername());
        Picasso.with(holder.itemView.getContext()).load(post.getImage()).resize(100,100).centerCrop().into(holder.mImageView);
        Log.d("Positiontag", "position: "+position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder{

        private TextView descriptionTextView;

        private ImageView mImageView;

        private TextView usernameTextView;



        public FeedViewHolder(View postView){

            super(postView);
            descriptionTextView = postView.findViewById(R.id.post_description);
            mImageView = postView.findViewById(R.id.post_image);
            usernameTextView = postView.findViewById(R.id.post_username);


            PhotoViewAttacher photoAttacher;
            photoAttacher = new PhotoViewAttacher(mImageView);
            photoAttacher.update();
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
