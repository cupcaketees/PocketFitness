package uk.ac.tees.cupcake.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        holder.postDescriptionTextView.setText(post.getDescription());
        holder.postDateTextView.setText(post.getDate());
        holder.postProfileNameTextView.setText(post.getFirstName() + " " + post.getLastName());
        CollectionReference path = FirebaseFirestore.getInstance().collection("Users/" + post.getUserUid() + "/User Posts/" + post.getPostId() + "/Likes");

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

        holder.postLikeButton.setOnClickListener(v -> {

            path.document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if(documentSnapshot.exists()){
                        path.document(auth.getCurrentUser().getUid())
                            .delete()
                            .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), "You remove your like", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                    }else{
                        Map<String, Object> likeTimeStamp = new HashMap<>();
                        likeTimeStamp.put("timestamp", FieldValue.serverTimestamp());

                        path.document(auth.getCurrentUser().getUid())
                            .set(likeTimeStamp)
                            .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), "You liked the post", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
        });

        path.document(auth.getCurrentUser().getUid()).addSnapshotListener((documentSnapshot, e) -> {
            if(documentSnapshot == null){
                return;
            }
            if(documentSnapshot.exists()){
                holder.postLikeButton.setText("Unlike");
            }else{
                holder.postLikeButton.setText("Like");
            }
        });

        path.addSnapshotListener((documentSnapshots, e) -> {
            if(documentSnapshots == null){
                return;
            }

            if(documentSnapshots.isEmpty()){
                holder.setPostLikesCount(0);
            }else{
                holder.setPostLikesCount(documentSnapshots.size());
            }
        });
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
        private TextView postLikesCountTextView;

        private Button postLikeButton;

        public FeedViewHolder(View postView) {
            super(postView);

            postProfileNameTextView = postView.findViewById(R.id.feed_post_username_text_view);
            postDescriptionTextView = postView.findViewById(R.id.feed_post_description_text_view);
            postImageImageView = postView.findViewById(R.id.feed_post_image_image_view);
            postDateTextView = postView.findViewById(R.id.feed_post_time_posted_text_view);
            postProfilePictureImageView = postView.findViewById(R.id.feed_post_profile_picture_image_view);
            postLikesCountTextView = postView.findViewById(R.id.feed_post_likes_count_text_view);
            postLikeButton = postView.findViewById(R.id.feed_post_like_button);
        }

        public void setPostLikesCount(int value){
            if(value == 1){
                postLikesCountTextView.setText(value + " person liked this post");
            }else{
                postLikesCountTextView.setText(value + " people liked this post");
            }
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