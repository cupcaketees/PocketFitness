package uk.ac.tees.cupcake.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
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
        String currentUserUid = auth.getCurrentUser().getUid();

        // Path to current post likes
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("Users/" + post.getUserUid() + "/User Posts/" + post.getPostId() + "/Likes");

        // calc time ago
        long time = post.getTimeStamp().getTime();
        long now = System.currentTimeMillis();
        CharSequence ago = DateUtils.getRelativeTimeSpanString(time,now , DateUtils.SECOND_IN_MILLIS);

        String profileName = post.getFirstName() + " " + post.getLastName();

        // Set values

        holder.postDescriptionTextView.setText(post.getDescription());
        holder.postDateTextView.setText(ago);
        holder.postProfileNameTextView.setText(profileName);

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

        // On click checks checks if current user uid document exists within selected post likes collection.
        holder.postLikeButton.setOnClickListener(v -> collectionRef.document(currentUserUid)
                     .get()
                     .addOnSuccessListener(documentSnapshot -> {

                         if(documentSnapshot.exists()){
                             // Deletes document if it already exists.
                             collectionRef.document(currentUserUid)
                                          .delete()
                                          .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), "You removed your like", Toast.LENGTH_SHORT).show())
                                          .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                         }else{
                             // Creates new document entry "like" with server timestamp if a document does not already exist.
                             Map<String, Object> likeTimeStamp = new HashMap<>();
                             likeTimeStamp.put("timestamp", FieldValue.serverTimestamp());

                             collectionRef.document(currentUserUid)
                                          .set(likeTimeStamp)
                                          .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), "You liked the post", Toast.LENGTH_SHORT).show())
                                          .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                         }
                     }));

        // Updates Like button Text
        collectionRef.document(currentUserUid)
                     .addSnapshotListener((documentSnapshot, e) -> {
                         if(documentSnapshot == null){
                             return;
                         }

                         String likeValue = documentSnapshot.exists() ? "Unlike" : "Like";
                         holder.postLikeButton.setText(likeValue);
                     });

        // Gets total amount of likes
        collectionRef.addSnapshotListener((documentSnapshots, e) -> {
            if(documentSnapshots == null){
                return;
            }

            int totalLikes = documentSnapshots.isEmpty() ? 0 : documentSnapshots.size();
            holder.setPostLikesCount(totalLikes);
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
        private TextView postLikesCountTextView;

        private ImageView postImageImageView;
        private ImageView postProfilePictureImageView;

        private Button postLikeButton;

        public FeedViewHolder(View postView) {
            super(postView);

            postProfileNameTextView = postView.findViewById(R.id.feed_post_username_text_view);
            postDescriptionTextView = postView.findViewById(R.id.feed_post_description_text_view);
            postDateTextView = postView.findViewById(R.id.feed_post_time_posted_text_view);
            postLikesCountTextView = postView.findViewById(R.id.feed_post_likes_count_text_view);

            postImageImageView = postView.findViewById(R.id.feed_post_image_image_view);
            postProfilePictureImageView = postView.findViewById(R.id.feed_post_profile_picture_image_view);

            postLikeButton = postView.findViewById(R.id.feed_post_like_button);
        }

        /**
         * Sets likes text view to appropriate output.
         * @param value total likes
         */
        public void setPostLikesCount(int value){
            String output = (value == 1) ? "1 person liked this post" : value + " people liked this post";
            postLikesCountTextView.setText(output);
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