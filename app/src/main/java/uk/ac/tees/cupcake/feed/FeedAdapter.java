package uk.ac.tees.cupcake.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.like.LikeButton;
import com.like.OnLikeListener;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.account.ViewProfileActivity;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private final List<Post> posts;
    private PopupWindow mDropdown;

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

        String currentUserUid = FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getUid();

        // Reference to current post likes collection
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("Users/" + post.getUserUid() + "/User Posts/" + post.getPostId() + "/Likes");

        // calc time ago
        long time = post.getTimeStamp().getTime();
        long now = System.currentTimeMillis();
        CharSequence ago = DateUtils.getRelativeTimeSpanString(time,now , DateUtils.SECOND_IN_MILLIS);

        FirebaseFirestore.getInstance()
                         .collection("Users")
                         .document(post.getUserUid())
                         .get()
                         .addOnSuccessListener(documentSnapshot -> {
                             UserProfile profile = documentSnapshot.toObject(UserProfile.class);

                             String profileName = profile.getFirstName() + " " + profile.getLastName();
                             holder.postProfileNameTextView.setText(profileName);

                             if(profile.getProfilePictureUrl() != null){
                                 Picasso.with(holder.itemView.getContext())
                                        .load(profile.getProfilePictureUrl())
                                        .into(holder.postProfilePictureImageView);
                             }

                         });


        // Set values
        holder.postDescriptionTextView.setText(post.getDescription());
        holder.postDateTextView.setText(ago);

        if(post.getImage() != null) {
            Picasso.with(holder.itemView.getContext())
                   .load(post.getImage())
                   .into(holder.postImageImageView);
        }

        // Set like button to correct value
        collectionRef.document(currentUserUid)
                     .get()
                     .addOnSuccessListener(documentSnapshot -> {
                         boolean value = documentSnapshot.exists();
                         holder.postLikeButton.setLiked(value);
                     });

        holder.postLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                // Creates new document entry "like" with server timestamp if a document does not already exist.
                Map<String, Object> likeTimeStamp = new HashMap<>();
                likeTimeStamp.put("timestamp", FieldValue.serverTimestamp());

                collectionRef.document(currentUserUid)
                             .set(likeTimeStamp)
                             .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), "You liked the post", Toast.LENGTH_SHORT).show())
                             .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                // Deletes document removing like
                collectionRef.document(currentUserUid)
                             .delete()
                             .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), "You removed your like", Toast.LENGTH_SHORT).show())
                             .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });

        // Updates Like button Text
        collectionRef.document(currentUserUid)
                     .addSnapshotListener((documentSnapshot, e) -> {
                         if(documentSnapshot == null){
                             return;
                         }

                         String likeValue = documentSnapshot.exists() ? "Unlike" : "Like";
                         holder.postLikeButtonTextView.setText(likeValue);
                     });

        // Gets total amount of likes
        collectionRef.addSnapshotListener((documentSnapshots, e) -> {
            if(e != null){
                return;
            }

            int totalLikes = documentSnapshots.isEmpty() ? 0 : documentSnapshots.size();
            holder.setPostLikesCount(totalLikes);
        });

        // More options on click . creates popup window
        holder.postMoreOptionsImageButton.setOnClickListener(v -> {

            try {
                LayoutInflater inflater = (LayoutInflater) holder.itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout;
                TextView optionOne;

                if(holder.mCurrentUser.getUid().equals(post.getUserUid())){
                    // Post is by current user
                     layout = inflater.inflate(R.layout.feed_more_option_menu_active_user, null);

                    optionOne = layout.findViewById(R.id.feed_more_option_remove_active);

                    // option one to remove post
                    optionOne.setOnClickListener(v1 -> {
                        holder.deletePost(post.getPostId());
                        mDropdown.dismiss();
                    });

                }else{
                    // Post is not by current user
                    layout = inflater.inflate(R.layout.feed_more_option_menu_user, null);
                    optionOne = layout.findViewById(R.id.feed_more_option_report);

                    optionOne.setOnClickListener(v1 -> {
                        holder.reportPost(post.getPostId());
                        mDropdown.dismiss();
                    });
                }

                layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

                Drawable background = holder.itemView.getResources().getDrawable(android.R.drawable.editbox_background_normal);
                mDropdown.setBackgroundDrawable(background);

                mDropdown.showAsDropDown(holder.postMoreOptionsImageButton, -300 ,-30);

            }catch(Exception e){
                e.printStackTrace();
            }
        });

        holder.postProfileNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ViewProfileActivity.class);
                intent.putExtra("profileId", post.getUserUid());
                v.getContext().startActivity(intent);
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
        private TextView postLikesCountTextView;
        private TextView postLikeButtonTextView;
        private Context context;

        private ImageView postImageImageView;
        private ImageView postProfilePictureImageView;

        private ImageButton postMoreOptionsImageButton;
        private LikeButton postLikeButton;

        private FirebaseUser mCurrentUser;

        public FeedViewHolder(View postView) {
            super(postView);

            postProfileNameTextView = postView.findViewById(R.id.feed_post_username_text_view);
            postDescriptionTextView = postView.findViewById(R.id.feed_post_description_text_view);
            postDateTextView = postView.findViewById(R.id.feed_post_time_posted_text_view);
            postLikesCountTextView = postView.findViewById(R.id.feed_post_likes_count_text_view);
            postLikeButtonTextView = postView.findViewById(R.id.feed_post_like_button_text_view);

            postImageImageView = postView.findViewById(R.id.feed_post_image_image_view);
            postProfilePictureImageView = postView.findViewById(R.id.feed_post_profile_picture_image_view);

            postMoreOptionsImageButton = postView.findViewById(R.id.feed_more_options_image_button);
            postLikeButton = postView.findViewById(R.id.post_like_button);

            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            context = postView.getContext();
        }

        /**
         * Sets likes text view to appropriate output.
         * @param value total likes
         */
        private void setPostLikesCount(int value){
            String output = (value == 1) ? "1 person liked this post" : value + " people liked this post";
            postLikesCountTextView.setText(output);
        }

        private void deletePost(String postId){

            DocumentReference documentRef = FirebaseFirestore.getInstance()
                                                             .collection("Users")
                                                             .document(mCurrentUser.getUid() + "/User Posts/" + postId);
            // First deletes all likes from a users post.
            documentRef.collection("Likes")
                       .get()
                       .addOnSuccessListener(documentSnapshots -> {

                           for(DocumentSnapshot documentSnapshot : documentSnapshots){
                               documentSnapshot.getReference().delete();
                           }
                       });
            // Deletes Post
            documentRef.delete()
                             .addOnSuccessListener(aVoid -> Toast.makeText(context, "Post has been removed.", Toast.LENGTH_SHORT).show())
                             .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
        }


        private void reportPost(String postId){
            Map<String, Object> reportTimeStamp = new HashMap<>();
            reportTimeStamp.put("timestamp", FieldValue.serverTimestamp());

            FirebaseFirestore.getInstance()
                             .collection("Reports")
                             .document(postId)
                             .collection("Reporters")
                             .document(mCurrentUser.getUid())
                             .set(reportTimeStamp)
                             .addOnSuccessListener(aVoid -> Toast.makeText(context, "You have reported the post.", Toast.LENGTH_SHORT).show())
                             .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
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