package uk.ac.tees.cupcake.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import uk.ac.tees.cupcake.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private final List<Post> posts;

    public FeedAdapter(List<Post> posts) {
        this.posts = posts;
        setHasStableIds(true);
    }

    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.post_row, parent, false);
        return new FeedViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.postDescriptionTextView.setText(post.getDescription());
        holder.postDateTextView.setText(post.getDate());
        holder.postProfileNameTextView.setText(post.getFirstName() + " " + post.getLastName());

        if(post.getProfilePictureUrl() != null){
            Picasso.with(holder.itemView.getContext())
                    .load(post.getProfilePictureUrl())
                    .into(holder.postProfilePictureImageView);
        }

        if(post.getImage() != null) {
            Picasso.with(holder.itemView.getContext())
                    .load(post.getImage())
                    .into(holder.postImageImageView);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        private TextView postDescriptionTextView;
        private TextView postDateTextView;
        private TextView postProfileNameTextView;

        private ImageView postImageImageView;
        private ImageView postProfilePictureImageView;

        public FeedViewHolder(View postView) {
            super(postView);

            postProfileNameTextView = postView.findViewById(R.id.feed_post_username_text_view);
            postDescriptionTextView = postView.findViewById(R.id.feed_post_description_text_view);
            postImageImageView = postView.findViewById(R.id.feed_post_image_image_view);
            postDateTextView = postView.findViewById(R.id.feed_post_time_posted_text_view);
            postProfilePictureImageView = postView.findViewById(R.id.feed_post_profile_picture_image_view);

//        private Button optionsButton;

//            descriptionTextView = postView.findViewById(R.id.post_description);
//            mImageView = postView.findViewById(R.id.post_image);
//            usernameTextView = postView.findViewById(R.id.feed_post_username_text_view);
//            dateTextView = postView.findViewById(R.id.post_date);
//            mProfilePic = postView.findViewById(R.id.feed_post_profile_picture_image_view);
//            mProfilePic.setImageResource(R.drawable.profilepic);
//            ratingCount = postView.findViewById(R.id.post_ratingcount);
//            ratingCount.setText("999");
//            optionsButton = postView.findViewById(R.id.dropdown_menu);
//
//            optionsButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    PopupMenu popupMenu = new PopupMenu(postView.getContext(), optionsButton);
//                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
//
//                    popupMenu.setOnMenuItemClickListener(item -> {
//                        Toast.makeText(postView.getContext(), "You clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                        return true;
//                    });
//                    popupMenu.show(); //showing popup menu
//                }
//            });
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